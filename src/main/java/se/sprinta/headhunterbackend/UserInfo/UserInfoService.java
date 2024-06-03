package se.sprinta.headhunterbackend.UserInfo;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import se.sprinta.headhunterbackend.UserInfo.dto.UserInfoDtoForm;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.UserRepository;
import se.sprinta.headhunterbackend.user.UserService;

@Service
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;

    public UserInfoService(UserInfoRepository userInfoRepository, UserRepository userRepository) {
        this.userInfoRepository = userInfoRepository;
        this.userRepository = userRepository;
    }

    public UserInfo getUserInfo(String email) {
        return this.userInfoRepository.getUserInfo(email)
                .orElseThrow(() -> new ObjectNotFoundException("userInfo", email));
    }

    public UserInfo updateUserInfo(UserInfoDtoForm userInfoDtoForm, String email) {
        UserInfo userInfo = this.userInfoRepository.getUserInfo(email)
                .orElseThrow(() -> new ObjectNotFoundException("user", email));

        userInfo.setName(userInfoDtoForm.name());
        userInfo.setOrganization(userInfoDtoForm.organization());

        return this.userInfoRepository.save(userInfo);
    }
}

