package se.sprinta.headhunterbackend.account.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import se.sprinta.headhunterbackend.account.Account;
import se.sprinta.headhunterbackend.account.dto.AccountDtoFormUpdate;

/**
 * User data needs to be mapped to User entity format.
 */

@Component
public class AccountDtoFormUpdateToAccountConverter implements Converter<AccountDtoFormUpdate, Account> {
    /**
     * Converts a given UserDtoForm to a User entity
     *
     * @param source UserDtoForm to convert
     * @return User entity with data from source
     */


    @Override
    public Account convert(AccountDtoFormUpdate source) {
        return new Account(source.roles());
    }
}
