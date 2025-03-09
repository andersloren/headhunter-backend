package se.sprinta.headhunterbackend.account;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jaxb.core.v2.TODO;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import se.sprinta.headhunterbackend.MockDatabaseInitializer;
import se.sprinta.headhunterbackend.TestUtils;
import se.sprinta.headhunterbackend.account.dto.AccountDtoFormRegister;
import se.sprinta.headhunterbackend.account.dto.AccountDtoFormUpdate;
import se.sprinta.headhunterbackend.account.dto.AccountDtoView;
import se.sprinta.headhunterbackend.account.dto.AccountUpdateDtoForm;
import se.sprinta.headhunterbackend.system.StatusCode;
import se.sprinta.headhunterbackend.system.exception.EmailAlreadyExistsException;
import se.sprinta.headhunterbackend.system.exception.EmailNotFreeException;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;
import se.sprinta.headhunterbackend.system.exception.TokenDoesNotExistException;
import se.sprinta.headhunterbackend.verification.VerificationService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("mock-test")
@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false) // Turns off Spring security
class AccountControllerMockTest {

    @MockBean
    private AccountService accountService;

    @MockBean
    private VerificationService verificationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Account> accounts = new ArrayList<>();
    private List<AccountDtoView> accountDtos = new ArrayList<>();
    private List<AccountDtoFormRegister> accountDtoFormRegister = new ArrayList<>();

    @Value("${api.endpoint.base-url-account}")
    String baseUrlAccount;

    @BeforeEach
    void setUp() {
        this.accounts = MockDatabaseInitializer.initializeMockAccounts();
        this.accountDtos = MockDatabaseInitializer.initializeMockAccountDtos();
        this.accountDtoFormRegister = MockDatabaseInitializer.initializeAccountDtoFormRegister();
    }

    @Test
    @DisplayName("Test Data Array Initializer")
    void test_DataInitializer() {
        System.out.println("AccountControllerMockTest, accounts size: " + this.accounts.size());
        for (Account account : this.accounts) {
            System.out.println(account.toString());
        }
        System.out.println("AccountControllerMockTest, accountDtos size: " + this.accountDtos.size());
        for (AccountDtoView accountDto : this.accountDtos) {
            System.out.println(accountDto.toString());
        }
        System.out.println("AccountControllerMockTest, accountDtoFormRegister size: " + this.accountDtoFormRegister.size());
        for (AccountDtoFormRegister accountDtoFormRegister : this.accountDtoFormRegister) {
            System.out.println(accountDtoFormRegister.toString());
        }
    }

    @Test
    @DisplayName("GET - findAll - Success")
    void test_findAll_Success() throws Exception {
        // Given
        given(this.accountService.findAll()).willReturn(this.accounts);

        // When and then
        ResultActions result = this.mockMvc
                .perform(get(this.baseUrlAccount + "/findAll").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Accounts Success"));

        JsonNode dataArray = TestUtils.getJSONResponse(result);

        for (int i = 0; i < dataArray.size(); i++) {
            result.andExpect(jsonPath("$.data[" + i + "].email").value(this.accounts.get(i).getEmail()))
                    .andExpect(jsonPath("$.data[" + i + "].password").isNotEmpty())
                    .andExpect(jsonPath("$.data[" + i + "].roles").value(this.accounts.get(i).getRoles()))
                    .andExpect(jsonPath("$.data[" + i + "].number_of_jobs").value(this.accounts.get(i).getNumber_of_jobs()));

        }
    }

    @Test
    @DisplayName("GET - getAccountDtos - Success")
    void test_GetAccountDtos_Success() throws Exception {
        // Given
        given(this.accountService.getAccountDtos()).willReturn(this.accountDtos);

        // When and then
        ResultActions result = this.mockMvc.perform(get(this.baseUrlAccount + "/getAccountDtos")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Account Dtos Success"));

        JsonNode dataArray = TestUtils.getJSONResponse(result);

        for (int i = 0; i < dataArray.size(); i++) {

            result.andExpect(jsonPath("$.data[" + i + "].email").value(this.accountDtos.get(i).email()))
                    .andExpect(jsonPath("$.data[" + i + "].password").doesNotExist())
                    .andExpect(jsonPath("$.data[" + i + "].number_of_jobs").value(this.accountDtos.get(i).number_of_jobs()))
                    .andExpect(jsonPath("$.data[" + i + "].roles").value(this.accountDtos.get(i).roles()));
        }
    }

    @Test
    @DisplayName("GET - findById - Success")
    void test_FindById_Success() throws Exception {
        Account expectedAccount = this.accounts.get(1);

        // Given
        given(this.accountService.findById("user1-test@hh.se")).willReturn(this.accounts.get(1));

        // When and then
        this.mockMvc
                .perform(get(this.baseUrlAccount + "/findById" + "/user1-test@hh.se")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Account By Id Success"))
                .andExpect(jsonPath("$.data.email").value(expectedAccount.getEmail()))
                .andExpect(jsonPath("$.data.password").isNotEmpty())
                .andExpect(jsonPath("$.data.roles").value(expectedAccount.getRoles()))
                .andExpect(jsonPath("$.data.number_of_jobs").value(expectedAccount.getNumber_of_jobs()));
    }

    @Test
    @DisplayName("GET - findById - Non-existent Email - Exception")
    void test_FindById_NonExistentEmail_Exception() throws Exception {
        // Given
        given(this.accountService.findById("abc"))
                .willThrow(new ObjectNotFoundException("account", "abc"));

        // When and then
        this.mockMvc.perform(get(this.baseUrlAccount + "/findById" + "/abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find account with Email abc"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("GET - validateEmailAvailable - Non-Existing Email - Success")
        // If the mail is non-existing, then it
        // can be registered, so it's a success
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
    @DisplayName("GET - validateEmailAvailable - Existing Email - Exception")
        // If the mail is existing, then it can not
        // be registered, causing an exception to be
        // thrown
    void test_ValidateEmailAvailable_ExistingEmail_Exception() throws Exception {
        // Given
        given(this.accountService.validateEmailAvailable("admin@hh.se"))
                .willThrow(new EmailNotFreeException("admin@hh.se"));

        // When and then
        this.mockMvc.perform(get(this.baseUrlAccount + "/validateEmailAvailable" + "/admin@hh.se")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.CONFLICT))
                .andExpect(jsonPath("$.message").value("admin@hh.se is already registered"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("PUT - registerAccount - Success")
    void test_RegisterAccount_Success() throws Exception {
        AccountDtoFormRegister newAccountFormRegister = new AccountDtoFormRegister(
                "newAccount@hh.se",
                "123",
                "user");

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
    @DisplayName("POST - register - Email Already Exists - Exception")
    void test_Register_EmailAlreadyExists_Exception() throws Exception {

        String json = this.objectMapper.writeValueAsString(this.accountDtoFormRegister.get(0));

        // Given
        given(this.accountService.register(any(AccountDtoFormRegister.class)))
                .willThrow(new EmailAlreadyExistsException());


        // When and then
        this.mockMvc.perform(post(this.baseUrlAccount + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("Email is already registered"));

    }


    @Test
    @DisplayName("GET - getAccountDtoByEmail - Success")
    void test_getAccountDtoByEmail_Success() throws Exception {
        AccountDtoView accountDtoView = this.accountDtos.get(1);

        // Given
        given(this.accountService.getAccountDtoByEmail("user1-test@hh.se")).willReturn(this.accountDtos.get(1));

        // When and then
        this.mockMvc.perform(get(this.baseUrlAccount + "/getAccountDtoByEmail" + "/user1-test@hh.se")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Account Dto Success"))
                .andExpect(jsonPath("$.data.email").value(accountDtoView.email()))
                .andExpect(jsonPath("$.data.password").doesNotExist())
                .andExpect(jsonPath("$.data.roles").value(accountDtoView.roles()))
                .andExpect(jsonPath("$.data.number_of_jobs").value(accountDtoView.number_of_jobs()));
    }

    @Test
    @DisplayName("GET - getAccountDtoByEmail - Non-existent Email - Exception")
    void test_GetAccountDtoByEmail_NonExistentEmail_Exception() throws Exception {
        String nonExistingEmail = "abc";

        // Given
        given(this.accountService.getAccountDtoByEmail("abc")).willThrow(new ObjectNotFoundException("account", "abc"));

        // When and then
        this.mockMvc
                .perform(get(this.baseUrlAccount + "/getAccountDtoByEmail" + "/" + nonExistingEmail)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find account with Email abc"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    // TODO Should probably be PUT, not a POST
    @Test
    @DisplayName("POST - update - Success")
    void test_update_Success() throws Exception {

        AccountDtoFormUpdate accountDtoFormUpdate = new AccountDtoFormUpdate("newRole"); // Role changes from 'user' to
        // 'admin'

        Account updatedAccount = new Account();
        updatedAccount.setEmail("user@hh.se");
        updatedAccount.setRoles("newRole");

        String json = this.objectMapper.writeValueAsString(accountDtoFormUpdate);

        // Given
        given(this.accountService.update(eq("user@hh.se"), Mockito.any(AccountUpdateDtoForm.class)))
                .willReturn(updatedAccount);

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

    // TODO Should probably be PUT, not a POST
    @Test
    @DisplayName("POST - update - Non-existent Email - Exception")
    void test_update_NonExistentEmail() throws Exception {

        AccountDtoFormUpdate accountDtoFormUpdate = new AccountDtoFormUpdate("newRole");

        String json = this.objectMapper.writeValueAsString(accountDtoFormUpdate);

        // Given
        given(this.accountService.update(eq("abc"), Mockito.any(AccountUpdateDtoForm.class)))
                .willThrow(new ObjectNotFoundException("account", "abc"));

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
    @DisplayName("DELETE - delete - Success")
    void test_delete_Success() throws Exception {

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
    @DisplayName("DELETE - delete - Non-existent Email - Exception")
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
