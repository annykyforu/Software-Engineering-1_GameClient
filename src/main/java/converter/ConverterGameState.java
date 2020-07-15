package converter;

import MessagesBase.ERequestState;
import MessagesBase.ResponseEnvelope;
import MessagesGameState.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ConverterGameState {
	private final Logger logger = LoggerFactory.getLogger(ConverterGameState.class);

	private GameState gameState;

    @SuppressWarnings("unchecked")
    public ConverterGameState(WebClient baseWebClient, String playerID){
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET)
				.uri("/states/" + playerID)
				.retrieve()
				.bodyToMono(ResponseEnvelope.class); // specify the object returned by the server

		ResponseEnvelope<GameState> requestResult = webAccess.block();

		if(requestResult.getState() == ERequestState.Error) {
			logger.error("Game State check failed: " + requestResult.getExceptionMessage());
		} else {
		    this.gameState = requestResult.getData().get();
        }
    }

    public GameState getGameState(){ return gameState; }
}
