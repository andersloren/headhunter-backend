package se.sprinta.headhunterbackend.UserInfo;

import org.springframework.web.bind.annotation.*;
import se.sprinta.headhunterbackend.UserInfo.converter.UserInfoToUserInfoDtoView;
import se.sprinta.headhunterbackend.UserInfo.dto.UserInfoDtoForm;
import se.sprinta.headhunterbackend.UserInfo.dto.UserInfoDtoView;
import se.sprinta.headhunterbackend.system.Result;
import se.sprinta.headhunterbackend.system.StatusCode;
import se.sprinta.headhunterbackend.user.dto.UserDtoForm;

@RestController
@RequestMapping("${api.endpoint.base-url-userInfo}")
@CrossOrigin("http://localhost:5173")
public class UserInfoController {

    private final UserInfoService userInfoService;

    private final UserInfoToUserInfoDtoView userInfoToUserInfoDtoView;

    public UserInfoController(UserInfoService userInfoService, UserInfoToUserInfoDtoView userInfoToUserInfoDtoView) {
        this.userInfoService = userInfoService;
        this.userInfoToUserInfoDtoView = userInfoToUserInfoDtoView;
    }

    @GetMapping("/getUserInfo/{email}")
    public Result getUserInfo(@PathVariable String email) {
        UserInfo returnedUserInfo = this.userInfoService.getUserInfo(email);
        UserInfoDtoView userInfoDtoView = this.userInfoToUserInfoDtoView.convert(returnedUserInfo);
        return new Result(true, StatusCode.SUCCESS, "Find UserInfo Success", userInfoDtoView);
    }

    @PutMapping("/updateUserInfo/{email}")
    public Result updateUserInfo(@PathVariable String email, @RequestBody UserInfoDtoForm userInfoDtoForm) {
        UserInfo savedUserInfo = this.userInfoService.updateUserInfo(userInfoDtoForm, email);
        UserInfoDtoView userInfoDtoView = this.userInfoToUserInfoDtoView.convert(savedUserInfo);
        return new Result(true, StatusCode.SUCCESS, "Save UserInfo Success", userInfoDtoView);
    }
}
