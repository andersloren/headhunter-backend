package se.sprinta.headhunterbackend.accountInfo;

import org.springframework.stereotype.Service;
import se.sprinta.headhunterbackend.accountInfo.dto.AccountInfoDtoForm;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

@Service
public class AccountInfoService {

    private final AccountInfoRepository accountInfoRepository;

    public AccountInfoService(AccountInfoRepository accountInfoRepository) {
        this.accountInfoRepository = accountInfoRepository;
    }

    public AccountInfo getAccountInfo(String email) {
        return this.accountInfoRepository.getAccountInfo(email)
                .orElseThrow(() -> new ObjectNotFoundException("accountInfo", email));
    }

    public AccountInfo updateAccountInfo(AccountInfoDtoForm accountInfoDtoForm, String email) {
        AccountInfo accountInfo = this.accountInfoRepository.getAccountInfo(email)
                .orElseThrow(() -> new ObjectNotFoundException("user", email));

        accountInfo.setName(accountInfoDtoForm.name());
        accountInfo.setOrganization(accountInfoDtoForm.organization());

        return this.accountInfoRepository.save(accountInfo);
    }
}

