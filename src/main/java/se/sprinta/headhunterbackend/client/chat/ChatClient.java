package se.sprinta.headhunterbackend.client.chat;

import se.sprinta.headhunterbackend.client.chat.dto.ChatRequest;
import se.sprinta.headhunterbackend.client.chat.dto.ChatResponse;

/**
 * The ChatClient interface interacts with an AI model (e.g., OpenAI GPT model) by sending requests and retrieving their responses.
 */
public interface ChatClient {

    /**
     * Returns a ChatResponse given a ChatRequest.
     *
     * @param chatRequest the input that guides an AI model to generate specific outputs
     * @return the generated completion from an AI model in a ChatResponse
     */
    ChatResponse generate(ChatRequest chatRequest);

}
