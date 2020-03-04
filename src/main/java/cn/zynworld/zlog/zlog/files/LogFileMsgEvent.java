package cn.zynworld.zlog.zlog.files;

import org.springframework.context.ApplicationEvent;

/**
 * @author zhaoyuening
 */
public class LogFileMsgEvent extends ApplicationEvent {
    private LogFileMsg logFileMsg;

    public LogFileMsgEvent(Object source, LogFileMsg logFileMsg) {
        super(source);
        this.logFileMsg = logFileMsg;
    }

    public LogFileMsg getLogFileMsg() {
        return logFileMsg;
    }
}
