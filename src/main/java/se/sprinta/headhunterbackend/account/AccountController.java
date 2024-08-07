package se.sprinta.headhunterbackend.account;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import se.sprinta.headhunterbackend.account.converter.AccountToAccountDtoViewConverter;
import se.sprinta.headhunterbackend.account.dto.AccountDtoFormRegister;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;
import se.sprinta.headhunterbackend.account.dto.AccountUpdateDtoForm;
import se.sprinta.headhunterbackend.system.Result;
import se.sprinta.headhunterbackend.system.StatusCode;

import java.util.List;

/**
 * Backend API endpoints for Account.
 */

@RestController
@RequestMapping("${api.endpoint.base-url-account}")
@CrossOrigin("http://localhost:5173")
public class AccountController {

    private final AccountService accountService;
    private final AccountToAccountDtoViewConverter accountToAccountDtoViewConverter;

    public AccountController(AccountService accountService, AccountToAccountDtoViewConverter accountToAccountDtoViewConverter) {
        this.accountService = accountService;
        this.accountToAccountDtoViewConverter = accountToAccountDtoViewConverter;
    }

    @GetMapping("/findAll")
    public Result findAll() {
        List<Account> foundAccounts = this.accountService.findAll();
        return new Result(true, StatusCode.SUCCESS, "Find All Accounts Success", foundAccounts);
    }

    @GetMapping("/getAccountDtos")
    public Result getAllAccountDtos() {
        List<AccountDtoView> foundAccountDtos = this.accountService.getAccountDtos();
        return new Result(true, StatusCode.SUCCESS, "Find Accounts Dtos Success", foundAccountDtos);
    }

    @GetMapping("/validateEmailAvailable/{email}")
    public Result validateEmailAvailable(@PathVariable String email) {
        this.accountService.validateEmailAvailable(email);
        return new Result(true, StatusCode.SUCCESS, "Email is available");
    }

    @GetMapping("/getAccountDtoByEmail/{userEmail}")
    public Result getAccountDtoByEmail(@PathVariable String userEmail) {
        AccountDtoView foundAccountDtoView = this.accountService.getAccountDtoByEmail(userEmail);
        return new Result(true, StatusCode.SUCCESS, "Find One Account Success", foundAccountDtoView);
    }

    @PostMapping("/register")
    public Result registerAccount(@RequestBody @Valid AccountDtoFormRegister accountDtoFormRegister) {
        Account addedAccount = this.accountService.register(accountDtoFormRegister);
        AccountDtoView addedAccountDtoView = this.accountToAccountDtoViewConverter.convert(addedAccount);
        return new Result(true, StatusCode.SUCCESS, "Add Account Success", addedAccountDtoView);
    }

    @PutMapping("/update/{email}")
    public Result updateAccount(@PathVariable String email, @RequestBody @NotNull AccountUpdateDtoForm accountUpdateDtoForm) {
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
