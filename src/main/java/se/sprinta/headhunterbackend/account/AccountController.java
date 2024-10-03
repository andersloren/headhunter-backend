package se.sprinta.headhunterbackend.account;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import se.sprinta.headhunterbackend.account.converter.AccountToAccountDtoViewConverter;
import se.sprinta.headhunterbackend.account.dto.AccountDtoFormRegister;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;
import se.sprinta.headhunterbackend.account.dto.AccountUpdateDtoForm;
import se.sprinta.headhunterbackend.system.Result;
import se.sprinta.headhunterbackend.system.StatusCode;
import jakarta.validation.constraints.NotNull;

/**
 * Backend API endpoints for Account.
 */

@RestController
@RequestMapping("${api.endpoint.base-url-account}")
@CrossOrigin(origins = "${CORS_ALLOWED_ORIGIN}"
        , methods = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE})

public class AccountController {

    private final AccountService accountService;
    private final AccountToAccountDtoViewConverter accountToAccountDtoViewConverter;

    public AccountController(AccountService accountService,
                             AccountToAccountDtoViewConverter accountToAccountDtoViewConverter) {
        this.accountService = accountService;
        this.accountToAccountDtoViewConverter = accountToAccountDtoViewConverter;
    }

    @GetMapping("/findAll")
    public Result findAll() {
        List<Account> foundAccounts = this.accountService.findAll();
        return new Result(true, StatusCode.SUCCESS, "Find All Accounts Success", foundAccounts);
    }

    @GetMapping("/getAccountDtos")
    public Result getAccountDtos() {
        List<AccountDtoView> foundAccountDtos = this.accountService.getAccountDtos();
        return new Result(true, StatusCode.SUCCESS, "Find Account Dtos Success", foundAccountDtos);
    }

    @GetMapping("/findById/{email}")
    public Result findAccount(@PathVariable String email) {
        Account foundAccount = this.accountService.findById(email);
        return new Result(true, StatusCode.SUCCESS, "Find Account By Id Success", foundAccount);
    }

    @GetMapping("/validateEmailAvailable/{email}")
    public Result validateEmailAvailable(@PathVariable String email) {
        this.accountService.validateEmailAvailable(email);
        return new Result(true, StatusCode.SUCCESS, "Email is available");
    }

    @GetMapping("/getAccountDtoByEmail/{userEmail}")
    public Result getAccountDtoByEmail(@PathVariable String userEmail) {
        AccountDtoView foundAccountDtoView = this.accountService.getAccountDtoByEmail(userEmail);
        return new Result(true, StatusCode.SUCCESS, "Find Account Dto Success", foundAccountDtoView);
    }

    @PostMapping("/register")
    public Result registerAccount(@RequestBody @Valid AccountDtoFormRegister accountDtoFormRegister) throws IOException, URISyntaxException {
        Account addedAccount = this.accountService.register(accountDtoFormRegister);
        AccountDtoView addedAccountDtoView = this.accountToAccountDtoViewConverter.convert(addedAccount);
        return new Result(true, StatusCode.SUCCESS, "Add Account Success", addedAccountDtoView);
    }

    @PutMapping("/update/{email}")
    public Result updateAccount(@PathVariable String email,
                                @RequestBody @NotNull AccountUpdateDtoForm accountUpdateDtoForm) {
        Account account = this.accountService.update(email, accountUpdateDtoForm);
        AccountDtoView updatedAccountDto = this.accountToAccountDtoViewConverter.convert(account);
        return new Result(true, StatusCode.SUCCESS, "Update Account Success", updatedAccountDto);
    }

    @DeleteMapping("/delete/{email}")
    public Result deleteAccount(@PathVariable String email) {
        this.accountService.delete(email);
        return new Result(true, StatusCode.SUCCESS, "Delete Account Success");
    }
}
