package ltd.nanoda.file.controller;

import com.mchange.v2.log.log4j.Log4jMLog;
import ltd.nanoda.file.model.Code;
import ltd.nanoda.file.model.FeedBack;
import ltd.nanoda.file.model.File;
import ltd.nanoda.file.model.User;
import ltd.nanoda.file.service.FIleService;
import ltd.nanoda.file.service.UserService;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;


@Controller
public class FileController {
    /**
     * 拦截文件列表请求
     * @return 首页
     */
    @RequestMapping(value = "/files/**", method = RequestMethod.GET)
    public String files() {
        return "filelist";
    }


    @Autowired
    UserService userService;

    /**
     * 上传
     * @param file 上传文件
     * @return
     */
    @RequestMapping(value = "/api/upload",method = RequestMethod.POST)
    @ResponseBody
    public FeedBack upLoadApi(MultipartFile file) {
        return fIleService.upLoad(file);

    }
    @RequestMapping(value = "/panel",method = RequestMethod.GET)
    public String upload(){
        return "panel";
    }


    /**
     * 请求下载
     * @param response
     * @param request
     */
    @RequestMapping(value = "/api/download/**", method = RequestMethod.GET)
    public void downloadApi(HttpServletResponse response, HttpServletRequest request) {

        //获取文件地址
        String path = (String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        AntPathMatcher apm = new AntPathMatcher();
        String finalPath = apm.extractPathWithinPattern(bestMatchPattern, path);

        //编码URL中文参数
        String coderStr = URLDecoder.decode(finalPath, StandardCharsets.UTF_8);
        System.out.println(coderStr);

        java.io.File file = new java.io.File("/home/nete/" + coderStr);
        fIleService.download(coderStr, response);


    }

    @Autowired
    FIleService fIleService;

    /**
     * 请求文件列表
     * @param request
     * @return 文件列表
     * @throws UnsupportedEncodingException 未知编码
     */
    @RequestMapping(value = "/api/**", method = RequestMethod.GET)
    @ResponseBody
    public List<File> filesApi(HttpServletRequest request) throws UnsupportedEncodingException {

        request.setCharacterEncoding("UTF-8");
        String path = (String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

        AntPathMatcher apm = new AntPathMatcher();
        String finalPath = apm.extractPathWithinPattern(bestMatchPattern, path);
        System.out.println(finalPath);

        String coderStr = URLDecoder.decode(finalPath, StandardCharsets.UTF_8);

        return fIleService.getFileList(coderStr);

    }
}
