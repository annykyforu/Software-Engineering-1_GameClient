package converter;

import MessagesBase.ERequestState;
import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniquePlayerIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ConverterPlayerReg {
    private final Logger logger = LoggerFactory.getLogger(ConverterPlayerReg.class);

    private UniquePlayerIdentifier playerID;

    @SuppressWarnings("unchecked")
    public ConverterPlayerReg(WebClient baseWebClient, String firstName, String lastName, String studentID){
        PlayerRegistration playerReg = new PlayerRegistration(firstName, lastName, studentID);

        Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST)
                .uri("/players")
                .body(BodyInserters.fromObject(playerReg))
                .retrieve()
                .bodyToMono(ResponseEnvelope.class);

        ResponseEnvelope<UniquePlayerIdentifier> resultPlayerReg = webAccess.block();

        if(resultPlayerReg.getState() == ERequestState.Error) {
            logger.error("Player registration failed: " + resultPlayerReg.getExceptionMessage());
        } else {
            logger.info("Player Registration: " + resultPlayerReg.getState());
            this.playerID = resultPlayerReg.getData().get();
        }

    }

    public String getPlayerID(){
        return playerID.getUniquePlayerID();
    }
}
