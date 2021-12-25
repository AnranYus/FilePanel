package ltd.nanoda.file.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import ltd.nanoda.file.dao.UserDaoMapper;
import ltd.nanoda.file.model.Code;
import ltd.nanoda.file.model.FeedBack;
import ltd.nanoda.file.model.User;
import ltd.nanoda.file.service.UserService;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.logging.LogManager;

@Service
public class UserServiceImpl implements UserService {
    private static final Log log = LogFactory.getLog(UserServiceImpl.class);
    @Autowired
    UserDaoMapper userDaoMapper;
    long ttlMillis = 1000 * 60 * 60 * 24;

    @Override
    public User getUserByUsername(String username) {
        return userDaoMapper.selectUserByUsername(username);

    }


    /**
     *
     * @param user 用户对象
     * @return 令牌
     */
    @Override
    public String IssueToken(User user) {

        long now = System.currentTimeMillis();

        Algorithm algorithmHS = Algorithm.HMAC256(user.getPassword());


        return JWT.create()
                .withIssuer(user.getUsername())
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(now + ttlMillis))
                .sign(algorithmHS);

    }

    /**
     *
     * @param user
     * @param token 令牌
     * @return 验证结果
     */
    @Override
    public FeedBack isVerify(User user, String token) {


        try {
            Algorithm algorithmHS = Algorithm.HMAC256(user.getPassword());
            JWTVerifier jwtVerifier = JWT.require(algorithmHS)
                    .withIssuer(user.getUsername())
                    .build();
            DecodedJWT jwt = jwtVerifier.verify(token);

            return new FeedBack(Code.OK, "isVerify",""+user.getType());
        } catch (Exception e) {
            log.error(e.fillInStackTrace());
            return new FeedBack(Code.TokenVerifyError, "isVerify", e.getMessage());
        }


    }
}
