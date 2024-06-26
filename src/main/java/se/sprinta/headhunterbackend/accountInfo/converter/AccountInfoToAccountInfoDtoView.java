package se.sprinta.headhunterbackend.accountInfo.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.accountInfo.AccountInfo;
import se.sprinta.headhunterbackend.accountInfo.dto.AccountInfoDtoView;

@Component
public class AccountInfoToAccountInfoDtoView implements Converter<AccountInfo, AccountInfoDtoView> {
    @Override
    public AccountInfoDtoView convert(AccountInfo source) {
        return new AccountInfoDtoView(
                source.getName(),
                source.getOrganization()
        );
    }
}
