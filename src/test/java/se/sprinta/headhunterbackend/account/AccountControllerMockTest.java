package se.sprinta.headhunterbackend.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.sprinta.headhunterbackend.MockDatabaseInitializer;
import se.sprinta.headhunterbackend.account.dto.AccountDtoFormRegister;
import se.sprinta.headhunterbackend.account.dto.AccountDtoFormUpdate;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;
import se.sprinta.headhunterbackend.account.dto.AccountUpdateDtoForm;
import se.sprinta.headhunterbackend.system.StatusCode;
import se.sprinta.headhunterbackend.system.exception.EmailNotFreeException;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

//@SpringBootTest
@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false) // Turns off Spring security
class AccountControllerMockTest {

    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Account> accounts = new ArrayList<>();
    private List<AccountDtoView> accountDtos = new ArrayList<>();

    @Value("${api.endpoint.base-url-account}")
    String baseUrlAccount;

    @BeforeEach
    void setUp() {
        this.accounts = MockDatabaseInitializer.initializeMockAccounts();
        this.accountDtos = MockDatabaseInitializer.initializeMockAccountDtos();
    }

    @Test
    @DisplayName("Test Data Initializer")
    void test_DataInitializer() {
        System.out.println("AccountControllerMockTest, accounts size: " + this.accounts.size());
        for (Account account : this.accounts) {
            System.out.println(account.toString());
        }
        System.out.println("AccountControllerMockTest, accountDtos size: " + this.accountDtos.size());
        for (AccountDtoView accountDto : this.accountDtos) {
            System.out.println(accountDto.toString());
        }
    }

    @Test
    @DisplayName("findAll - Success")
    void test_findAll_Success() throws Exception {
        // Given
        given(this.accountService.findAll()).willReturn(this.accounts);

        // When and then
        this.mockMvc.perform(get(this.baseUrlAccount + "/findAll").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Accounts Success"))
                .andExpect(jsonPath("$.data[0].email").value("admin-mock@hh.se"))
                .andExpect(jsonPath("$.data[0].password").isNotEmpty())
                .andExpect(jsonPath("$.data[0].roles").value("admin"))
                .andExpect(jsonPath("$.data[0].number_of_jobs").value(0))
                .andExpect(jsonPath("$.data[1].email").value("user1-mock@hh.se"))
                .andExpect(jsonPath("$.data[1].password").isNotEmpty())
                .andExpect(jsonPath("$.data[1].roles").value("user"))
                .andExpect(jsonPath("$.data[1].number_of_jobs").value(2))
                .andExpect(jsonPath("$.data[2].email").value("user2-mock@hh.se"))
                .andExpect(jsonPath("$.data[2].password").isNotEmpty())
                .andExpect(jsonPath("$.data[2].roles").value("user"))
                .andExpect(jsonPath("$.data[2].number_of_jobs").value(1))
                .andExpect(jsonPath("$.data[3].email").value("user3-mock@hh.se"))
                .andExpect(jsonPath("$.data[3].password").isNotEmpty())
                .andExpect(jsonPath("$.data[3].roles").value("user"))
                .andExpect(jsonPath("$.data[3].number_of_jobs").value(1));
    }

    @Test
    @DisplayName("getAccountById - Success")
    void test_GetAccountById_Success() throws Exception {
        AccountDtoView accountDtoView = this.accountDtos.get(1);

        // Given
        given(this.accountService.getAccountDtoByEmail("user1-mock@hh.se")).willReturn(accountDtoView);

        // When and then
        this.mockMvc.perform(get(this.baseUrlAccount + "/getAccountDtoByEmail" + "/user1-mock@hh.se").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Account Success"))
                .andExpect(jsonPath("$.data.email").value(this.accountDtos.get(1).email()))
                .andExpect(jsonPath("$.data.roles").value(this.accountDtos.get(1).roles()))
                .andExpect(jsonPath("$.data.number_of_jobs").value(this.accountDtos.get(1).number_of_jobs()));
    }

    @Test
    @DisplayName("getAccountById - Non-existent Id (Exception)")
    void test_FindAccountById_NonExistentId() throws Exception {
        String nonExistingEmail = "abc";

        // Given
        given(this.accountService.getAccountDtoByEmail("abc")).willThrow(new ObjectNotFoundException("account", "abc"));

        // When and then
        this.mockMvc.perform(get(this.baseUrlAccount + "/getAccountDtoByEmail" + "/" + nonExistingEmail).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find account with Email abc"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("validateEmailAvailable - Non-Existing Email - Success")
    void test_ValidateEmailAvailable_NonExistingEmail_Success() throws Exception {
        // Given
        given(this.accountService.validateEmailAvailable("availableEmail@hh.se")).willReturn(true);

        // When and then
        this.mockMvc.perform(get(this.baseUrlAccount + "/validateEmailAvailable" + "/availableEmail@hh.se")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Email is available"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("validateEmailAvailable - Existing Email - Exception")
    void test_ValidateEmailAvailable_ExistingEmail_Exception() throws Exception {
        // Given
        given(this.accountService.validateEmailAvailable("admin@hh.se")).willThrow(new EmailNotFreeException("admin@hh.se"));

        // When and then
        this.mockMvc.perform(get(this.baseUrlAccount + "/validateEmailAvailable" + "/admin@hh.se")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.CONFLICT))
                .andExpect(jsonPath("$.message").value("admin@hh.se is already registered"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("registerAccount")
    void test_RegisterAccount_Success() throws Exception {
        AccountDtoFormRegister newAccountFormRegister = new AccountDtoFormRegister(
                "newAccount@hh.se",
                "123"

        );

        Account registeredAccount = new Account();
        registeredAccount.setEmail("newAccount@hh.se");
        registeredAccount.setRoles("user");

        String json = this.objectMapper.writeValueAsString(newAccountFormRegister);

        // Given
        given(this.accountService.register(any(AccountDtoFormRegister.class))).willReturn(registeredAccount);

        // When and then
        this.mockMvc.perform(post(this.baseUrlAccount + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Account Success"))
                .andExpect(jsonPath("$.data.email").value("newAccount@hh.se"))
                .andExpect(jsonPath("$.data.roles").value("user"));
    }

    @Test
    void test_UpdateAccount_Success() throws Exception {

        AccountDtoFormUpdate accountDtoFormUpdate = new AccountDtoFormUpdate("newRole"); // Role changes from 'user' to 'admin'

        Account updatedAccount = new Account();
        updatedAccount.setEmail("user@hh.se");
        updatedAccount.setRoles("newRole");

        String json = this.objectMapper.writeValueAsString(accountDtoFormUpdate);

        // Given
        given(this.accountService.update(eq("user@hh.se"), Mockito.any(AccountUpdateDtoForm.class))).willReturn(updatedAccount);

        // When and then
        this.mockMvc.perform(put(this.baseUrlAccount + "/update" + "/user@hh.se")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Account Success"))
                .andExpect(jsonPath("$.data.email").value("user@hh.se"))
                .andExpect(jsonPath("$.data.roles").value("newRole"));
    }

    @Test
    void test_UpdateAccount_NonExistentEmail() throws Exception {

        AccountDtoFormUpdate accountDtoFormUpdate = new AccountDtoFormUpdate("newRole");

        String json = this.objectMapper.writeValueAsString(accountDtoFormUpdate);

        // Given
        given(this.accountService.update(eq("abc"), Mockito.any(AccountUpdateDtoForm.class))).willThrow(new ObjectNotFoundException("account", "abc"));

        // When and then
        this.mockMvc.perform(put(this.baseUrlAccount + "/update" + "/abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find account with Email abc"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void test_DeleteAccountById_Success() throws Exception {

        // Given
        willDoNothing().given(this.accountService).delete("abc");

        // When and then
        this.mockMvc.perform(delete(this.baseUrlAccount + "/delete" + "/user@hh.se")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Account Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void test_DeleteAccountByI_NonExistingId() throws Exception {
        // Given
        doThrow(new ObjectNotFoundException("account", "abc")).when(this.accountService).delete("abc");

        // When and then
        this.mockMvc.perform(delete(this.baseUrlAccount + "/delete" + "/abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find account with Email abc"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}