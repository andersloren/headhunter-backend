package se.sprinta.headhunterbackend.ad;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import se.sprinta.headhunterbackend.account.Account;
import se.sprinta.headhunterbackend.job.Job;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Turns off Spring security
class AdControllerMockTest {

    @MockBean
    private AdService adService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

    }

}