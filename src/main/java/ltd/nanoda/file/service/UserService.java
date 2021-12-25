package ltd.nanoda.file.service;

import ltd.nanoda.file.model.FeedBack;
import ltd.nanoda.file.model.User;

public interface UserService {
    User getUserByUsername(String username);

    String IssueToken(User user);

    FeedBack isVerify(User user, String token);
}
