package ltd.nanoda.file.controller;

import ltd.nanoda.file.model.Code;
import ltd.nanoda.file.model.FeedBack;
import ltd.nanoda.file.model.User;
import ltd.nanoda.file.service.UserService;
import ltd.nanoda.file.service.impl.UserServiceImpl;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

@Controller
public class UserController {
    private static final Log log = LogFactory.getLog(UserServiceImpl.class);


    @Autowired
    UserService userService;

    /**
     * 登录请求
     * @param password 密码
     * @param username 账户
     * @return token
     */
    @RequestMapping(value = "/api/login")
    @ResponseBody
    public User loginApi(String password,String username,HttpServletRequest request) {
        log.info(request.getRemoteAddr()+":"+request.getRemoteHost());

        User user = null;
        if (password != null && username != null) {
            try {

                user = userService.getUserByUsername(username);
                if (user.getPassword().equals(password)) {
                    String token = userService.IssueToken(user);
                    user.setToken(token);
                    user.setPassword(null);
                }

            } catch (Exception e) {
               log.error(e.fillInStackTrace());
            }

        }

        return user;

    }

    /**
     * 验证令牌
     * @param username 用户名
     * @param token 令牌
     * @return 验证结果
     */
    @RequestMapping(value = "/api/verify", method = RequestMethod.POST)
    @ResponseBody
    public FeedBack verifyApi(String username,String token) {

        User user = null;
        if (username != null && token != null) {
            try{
                user = userService.getUserByUsername(username);
            }catch (Exception e){
                log.error(e.fillInStackTrace());
                return new FeedBack(Code.NotFindUser,"verifyApi","Not find user by username");
            }
            return userService.isVerify(user, token);
        }
        return new FeedBack(Code.Error, "verifyApi", "Incomplete information ");
    }


}
