package cn.zynworld.zlog.zlog.connect;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import cn.zynworld.zlog.zlog.files.LogFileManager;
import cn.zynworld.zlog.zlog.files.LogFileMsg;
import cn.zynworld.zlog.zlog.files.LogFileMsgEvent;
import cn.zynworld.zlog.zlog.files.LogFileSourceImpl;

/**
 * @author zhaoyuening
 */
@Component
@ServerEndpoint(value = "/ws/{sourceId}")
public class WebSocketEndpoint implements ApplicationContextAware  {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketEndpoint.class);


    private static LogFileManager logFileManager;
    private static Map<String, WebSocketEndpoint> endpointMap = new ConcurrentHashMap<>();

    private Session session;
    private String sourceId;

    @OnOpen
    public void onOpen(Session session, @PathParam("sourceId") String sourceId) throws IOException {
        this.session = session;
        this.sourceId = sourceId;

        if (!logFileManager.containsSourceId(sourceId)) {
            session.close();
        }

        endpointMap.put(session.getId(), this);
    }

    @OnClose
    public void onClose() {
        endpointMap.remove(session.getId());
    }

    public void acceptMsg(LogFileMsg logFileMsg) {
        try {
            if (!logFileMsg.getSourceId().equals(sourceId)) return;
            session.getBasicRemote().sendText(logFileMsg.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Collection<WebSocketEndpoint> getEndpoints() {
        return endpointMap.values();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logFileManager = applicationContext.getBean(LogFileManager.class);
    }

}
