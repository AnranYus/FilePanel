package ltd.nanoda.file.service;

import ltd.nanoda.file.model.FeedBack;
import ltd.nanoda.file.model.File;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface FIleService {
    List<File> getFileList(String file);

    FeedBack download(String coderStr, HttpServletResponse response);

    FeedBack upLoad(MultipartFile file);
}
