package converter;

import MessagesBase.ERequestState;
import MessagesBase.PlayerMove;
import MessagesBase.ResponseEnvelope;
import MessagesGameState.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ConverterSendMove {
    private final Logger logger = LoggerFactory.getLogger(ConverterSendMove.class);

    public ConverterSendMove(WebClient baseWebClient, PlayerMove playerMove){
        Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST)
                .uri("/moves")
                .body(BodyInserters.fromObject(playerMove))
                .retrieve()
                .bodyToMono(ResponseEnvelope.class);

        @SuppressWarnings("unchecked")
        ResponseEnvelope<GameState> requestResult = webAccess.block();
        if(requestResult.getState() == ERequestState.Error) {
            logger.error("Make move failed: " + requestResult.getExceptionMessage());
        }
        else {
            logger.info("Result of my move: " + requestResult.getState());
        }
    }
}
