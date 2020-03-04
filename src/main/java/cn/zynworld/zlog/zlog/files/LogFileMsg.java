package cn.zynworld.zlog.zlog.files;

import lombok.Builder;
import lombok.Data;

/**
 * @author zhaoyuening
 */
@Data
@Builder
public class LogFileMsg{
    /**
     * 消息来源id
     */
    private String sourceId;
    /**
     * 消息内容
     */
    private String content;
}
