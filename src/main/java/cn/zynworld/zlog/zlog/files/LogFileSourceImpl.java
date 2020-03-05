package cn.zynworld.zlog.zlog.files;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import lombok.Cleanup;

/**
 * @author zhaoyuening
 */
public class LogFileSourceImpl implements LogFileSource {
    private static final Logger LOG = LoggerFactory.getLogger(LogFileSourceImpl.class);

    private LogFile logFile;

    // 上一次读取日志文件位置
    private long lastTimeFileSize = 0;

    public LogFileSourceImpl(LogFile logFile) {
        this.logFile = logFile;
    }

    @Override
    public LogFileMsg read() {
        try {
            @Cleanup RandomAccessFile file = getRandomAccessFile();
            // 当日志文件重置后
            if (file.length() < lastTimeFileSize) {
                lastTimeFileSize = file.length();
            }

            file.seek(lastTimeFileSize);
            StringBuilder logContent = new StringBuilder();
            String tmp = "";
            while ((tmp = file.readLine()) != null) {
                logContent.append(new String(tmp.getBytes("ISO8859-1"))).append("\n");
            }

            // 更新最近一次文件位置
            lastTimeFileSize = file.length();

            // 无新内容返回空
            if (logContent.toString().length() <= 0) return null;

            return LogFileMsg.builder()
                    .sourceId(logFile.getSourceId())
                    .content(logContent.toString())
                    .build();
        } catch (FileNotFoundException e) {
            LOG.info("日志文件暂未存在 logFile:{}", logFile);
            return null;
        } catch (Exception e) {
            LOG.info("日志文件读取异常 logFile:{}", logFile, e);
            return null;
        }
    }

    private RandomAccessFile getRandomAccessFile() throws FileNotFoundException {
        return new RandomAccessFile(logFile.getFile(), "r");
    }
}
