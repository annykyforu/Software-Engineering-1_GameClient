package converter;

import MessagesBase.ERequestState;
import MessagesBase.HalfMap;
import MessagesBase.ResponseEnvelope;
import MessagesGameState.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ConverterSendHalfMap {
    private final Logger logger = LoggerFactory.getLogger(ConverterSendHalfMap.class);

    @SuppressWarnings("unchecked")
    public ConverterSendHalfMap(WebClient baseWebClient, HalfMap playersMap) {
        Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST)
                .uri("/halfmaps")
                .body(BodyInserters.fromObject(playersMap)) // specify the data which is set to the server
                .retrieve()
                .bodyToMono(ResponseEnvelope.class); // specify the object returned by the server

        ResponseEnvelope<GameState> requestResult = webAccess.block();

        if(requestResult.getState() == ERequestState.Error) {
            logger.error("Send player HalfMap failed: " + requestResult.getExceptionMessage());
        } else {
            logger.info("Success! Player HalfMap received: " + requestResult.getState());
        }
    }



}
