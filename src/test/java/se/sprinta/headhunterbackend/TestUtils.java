package se.sprinta.headhunterbackend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestUtils {

  public static JsonNode getJSONResponse(ResultActions resultActions) throws Exception {

    MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
    String jsonResponse = mvcResult.getResponse().getContentAsString();

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode rootNode = objectMapper.readTree(jsonResponse);
    return rootNode.path("data");
  }
}
