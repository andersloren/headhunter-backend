package se.sprinta.headhunterbackend.accountInfo;

import org.springframework.web.bind.annotation.*;
import se.sprinta.headhunterbackend.accountInfo.converter.AccountInfoToAccountInfoDtoView;
import se.sprinta.headhunterbackend.accountInfo.dto.AccountInfoDtoForm;
import se.sprinta.headhunterbackend.accountInfo.dto.AccountInfoDtoView;
import se.sprinta.headhunterbackend.system.Result;
import se.sprinta.headhunterbackend.system.StatusCode;

@RestController
@RequestMapping("${api.endpoint.base-url-accountInfo}")
@CrossOrigin(origins = "${CORS_ALLOWED_ORIGIN}"
        , methods = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE})
public class AccountInfoController {

    private final AccountInfoService accountInfoService;

    private final AccountInfoToAccountInfoDtoView accountInfoToAccountInfoDtoView;

    public AccountInfoController(AccountInfoService accountInfoService, AccountInfoToAccountInfoDtoView accountInfoToAccountInfoDtoView) {
        this.accountInfoService = accountInfoService;
        this.accountInfoToAccountInfoDtoView = accountInfoToAccountInfoDtoView;
    }

    @GetMapping("/getAccountInfo/{email}")
    public Result getAccountInfo(@PathVariable String email) {
        AccountInfo returnedAccountInfo = this.accountInfoService.getAccountInfo(email);
        AccountInfoDtoView accountInfoDtoView = this.accountInfoToAccountInfoDtoView.convert(returnedAccountInfo);
        return new Result(true, StatusCode.SUCCESS, "Find AccountInfo Success", accountInfoDtoView);
    }

    @PutMapping("/updateAccountInfo/{email}")
    public Result updateAccountInfo(@PathVariable String email, @RequestBody AccountInfoDtoForm accountInfoDtoForm) {
        AccountInfo savedAccountInfo = this.accountInfoService.updateAccountInfo(accountInfoDtoForm, email);
        AccountInfoDtoView accountInfoDtoView = this.accountInfoToAccountInfoDtoView.convert(savedAccountInfo);
        return new Result(true, StatusCode.SUCCESS, "Save AccountInfo Success", accountInfoDtoView);
    }
}
