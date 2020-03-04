package cn.zynworld.zlog.zlog.connect;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import cn.zynworld.zlog.zlog.files.LogFileMsgEvent;

/**
 * @author zhaoyuening
 */
@Component
public class LogMsgEventManager implements ApplicationListener<LogFileMsgEvent> {

    @Override
    public void onApplicationEvent(LogFileMsgEvent logFileMsgEvent) {
        WebSocketEndpoint.getEndpoints().forEach(endpoint -> {endpoint.acceptMsg(logFileMsgEvent.getLogFileMsg());});
    }
}
