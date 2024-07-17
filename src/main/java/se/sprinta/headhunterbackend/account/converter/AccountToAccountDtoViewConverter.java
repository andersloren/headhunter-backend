package se.sprinta.headhunterbackend.account.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.account.Account;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;


/**
 * When requesting a User object, the User object data needs to be mapped to a UserDtoView object.
 */

@Component
public class AccountToAccountDtoViewConverter implements Converter<Account, AccountDtoView> {

    /**
     * Converts a given User to a UserDtoView entity
     *
     * @param source User to convert
     * @return UserDtoView Dto object with data from source
     */
    @Override
    public AccountDtoView convert(Account source) {
        return new AccountDtoView(
                source.getEmail(),
                source.getRoles(),
                source.getNumber_of_jobs()
        );
    }
}
