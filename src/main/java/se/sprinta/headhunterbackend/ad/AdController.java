package se.sprinta.headhunterbackend.ad;

import org.springframework.web.bind.annotation.*;
import se.sprinta.headhunterbackend.system.Result;
import se.sprinta.headhunterbackend.system.StatusCode;
import se.sprinta.headhunterbackend.user.User;
import se.sprinta.headhunterbackend.user.converter.UserToUserDtoViewConverter;
import se.sprinta.headhunterbackend.user.dto.UserDtoView;

@RestController
@RequestMapping("${api.endpoint.base-url-ads}")
@CrossOrigin("http://localhost:3000")
public class AdController {

    private AdService adService;
    private UserToUserDtoViewConverter userToUserDtoViewConverter;

    public AdController(AdService adService, UserToUserDtoViewConverter userToUserDtoViewConverter) {
        this.adService = adService;
        this.userToUserDtoViewConverter = userToUserDtoViewConverter;
    }

    @GetMapping(value = "/findUserByAdId/{adId}")
    public Result findUserByAdId(@PathVariable String adId) {
        User foundUser = this.adService.findUserByAdId(adId);
        UserDtoView foundUserDtoView = this.userToUserDtoViewConverter.convert(foundUser);
        return new Result(true, StatusCode.SUCCESS, "Find User By Ad Id Success", foundUserDtoView);
    }
}
