package cn.zynworld.zlog.zlog.files;

import java.io.File;
import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author zhaoyuening
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LogFile {

    private String sourceId;
    private String filePath;

    public File getFile() throws FileNotFoundException {
        return ResourceUtils.getFile(this.filePath);
    }
}
