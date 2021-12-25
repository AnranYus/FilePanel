package ltd.nanoda.file.service.impl;

import com.mchange.v2.log.log4j.Log4jMLog;
import ltd.nanoda.file.controller.FileController;
import ltd.nanoda.file.model.Code;
import ltd.nanoda.file.model.FeedBack;
import ltd.nanoda.file.model.File;
import ltd.nanoda.file.service.FIleService;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

@Service
public class FileServiceImpl implements FIleService {
    private static final Log log = LogFactory.getLog(FileServiceImpl.class);

    final String ROOTPATH = "/upload_files/";

    public String getSize(long size) {
        long rest = 0;
        if (size < 1024) {
            return size + "B";
        } else {
            size /= 1024;
        }

        if (size < 1024) {
            return size + "KB";
        } else {
            rest = size % 1024;
            size /= 1024;
        }

        if (size < 1024) {
            size = size * 100;
            return size / 100 + "." + rest * 100 / 1024 % 100 + "MB";
        } else {
            size = size * 100 / 1024;
            return size / 100 + "." + size % 100 + "GB";
        }
    }

    /**
     * @param coderStr 路径
     * @return 文件列表
     */
    @Override
    public List<File> getFileList(String coderStr) {
        String path = ROOTPATH+coderStr;
        log.warn(path);
        java.io.File file = new java.io.File(path);
        java.io.File[] files = file.listFiles();
        List<File> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        if (files != null) {
            for (java.io.File f : files) {
                if (f.isDirectory()) {
                    File mFile = new File(f.getName(), "directory", sdf.format(new Date(f.lastModified())));
                    list.add(mFile);
                } else {
                    File mFile = new File(f.getName(), "file", getSize(f.length()), sdf.format(new Date(f.lastModified())));
                    list.add(mFile);
                }
            }
        }

        return list;
    }

    /**
     * 下载文件
     *
     * @param coderStr     目标文件
     * @param response response对象
     * @return 返回信息
     */
    @Override
    public FeedBack download(String coderStr, HttpServletResponse response) {
        java.io.File file = new java.io.File(ROOTPATH+coderStr);
        response.setHeader("content-length", String.valueOf(file.length()));
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = response.getOutputStream();
            byte[] buf = new byte[4096];
            while (true) {

                int n = fis.read(buf);
                if (n <= 0)
                    break;

                os.write(buf, 0, n);
                os.flush();
            }

            os.close();
            os.close();

        } catch (IOException e) {
            log.error(e.fillInStackTrace());

            return new FeedBack(Code.NotFindFile, "getFile", e.getMessage());
        }
        return new FeedBack(Code.OK, "getFIle");

    }

    private FeedBack writerFile(java.io.File localFile,String name,MultipartFile file){
        Path filePath = Paths.get(localFile.getPath(), name);
        try {
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, filePath);
            System.out.println("true:" + filePath);

        } catch (FileAlreadyExistsException e) {
            log.error(e.getStackTrace());
            return new FeedBack(Code.FileAlreadyExists, "upLoad", e.getMessage());
        } catch (IOException e) {
            log.error(e.getStackTrace());

            return new FeedBack(Code.Error, "upLoad", e.getMessage());
        }
        return new FeedBack(Code.OK, "upLoad");

    }

    /**
     * @param file 文件
     * @return 返回信息
     */
    @Override
    public FeedBack upLoad(MultipartFile file) {
        String name = file.getOriginalFilename();
        String[] arr = name.split("\\.");
        String end = arr[arr.length - 1];
        end = end.toUpperCase(Locale.ROOT);
        String path = ROOTPATH+"files/" + end;
        log.warn("Up load path is "+path);

        java.io.File localFile = new java.io.File(path);

        try {
            boolean isMake = true;
            if (!localFile.exists()) {

                isMake =  localFile.mkdirs();
            }
            log.warn("mkdir is "+isMake);

            if (isMake) {
                return writerFile(localFile,name,file);
            }


        }catch (Exception e){
            log.error(e.getStackTrace());
            return new FeedBack(Code.Error, "upLoad", e.getMessage());
        }
        return new FeedBack(Code.Error,"upLoad","Has error in method");



    }
}
