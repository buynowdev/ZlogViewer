package cn.zynworld.zlog.zlog.files;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.zynworld.zlog.zlog.common.FileUtils;
import cn.zynworld.zlog.zlog.common.JsonUtils;

/**
 * @author zhaoyuening
 */
@Component
@RestController
@EnableScheduling
public class LogFileManager {

    @Autowired
    private Environment environment;
    @Autowired
    private ApplicationContext context;

    private Map<String, LogFile> logFileMap;
    private List<LogFileSource> logFileSources;
    private ExecutorService executorService;

    @PostConstruct
    public void init() {
        LogFileConfig config = readConfig();
        this.logFileMap = config.getLogFiles().stream()
                .collect(Collectors.toMap(LogFile::getSourceId, logFile -> logFile));
        this.logFileSources = config.getLogFiles().stream()
                .map(LogFileSource::build)
                .collect(Collectors.toList());
        this.logFileSources.forEach(LogFileSource::read);
        this.executorService = Executors.newFixedThreadPool(logFileSources.size());
    }

    @Scheduled(fixedRate = 500)
    public void readLogs() {
        logFileSources.forEach(logFileSource -> {
            executorService.execute(new LogFileSourceReadRunnable(logFileSource,context));
        });
    }


    /**
     * 被执行的读取任务
     */
    private static class LogFileSourceReadRunnable implements Runnable {
        public LogFileSourceReadRunnable(LogFileSource logFileSource, ApplicationContext context) {
            this.logFileSource = logFileSource;
            this.context = context;
        }

        private LogFileSource logFileSource;
        private ApplicationContext context;

        @Override
        public void run() {
            LogFileMsg msg = logFileSource.read();
            if (Objects.isNull(msg)) return;
            LogFileMsgEvent event = new LogFileMsgEvent(this, msg);
            context.publishEvent(event);
        }
    }


    public boolean containsSourceId(String sourceId) {
        return logFileMap.containsKey(sourceId);
    }

    /**
     * 返回所有的sourceId
     */
    @GetMapping("sourceIds")
    public Collection<String> querySourceIds() {
        return logFileMap.keySet();
    }

    private LogFileConfig readConfig() {
        String configPath = environment.getProperty("zlog.config");
        String configJson = FileUtils.readFileContent(configPath);
        return JsonUtils.jsonToPojo(configJson, LogFileConfig.class);
    }
}
