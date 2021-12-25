package ltd.nanoda.file;

import ltd.nanoda.file.dao.UserDaoMapper;
import ltd.nanoda.file.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FileApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    UserDaoMapper userDaoMapper;

    @Test
    void test(){
        User user =  userDaoMapper.selectUserByUsername("root");
        System.out.println(user.getUsername());
    }

}
