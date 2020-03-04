package cn.zynworld.zlog.zlog.files;

/**
 * @author zhaoyuening
 */
public interface LogFileSource {
    /**
     * 从日志文件中读取最新的消息
     * @return 日志文件消息
     */
    LogFileMsg read();

    static LogFileSource build(LogFile logFile) {
        return new LogFileSourceImpl(logFile);
    }
}
