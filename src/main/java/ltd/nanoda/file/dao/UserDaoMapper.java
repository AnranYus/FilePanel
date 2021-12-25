package ltd.nanoda.file.dao;

import ltd.nanoda.file.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDaoMapper {
    User selectUserByUsername(String username);
}
