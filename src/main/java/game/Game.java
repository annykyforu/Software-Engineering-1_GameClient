package game;

import MessagesBase.EMove;
import MessagesBase.HalfMapNode;
import MessagesBase.PlayerMove;
import MessagesGameState.*;
import ai.FindEnemyFort;
import ai.FindTreasure;
import converter.ConverterPlayerReg;
import converter.ConverterSendHalfMap;
import converter.ConverterSendMove;
import map.FullGameMap;
import map.PlayersMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

public class Game {
    private final Logger logger = LoggerFactory.getLogger(Game.class);
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

    private final WebClient baseWebClient;
    private GameState gameState;
    private final String gameStateID;
    private FullGameMap gameMap;
    private final Player player;
    private FindTreasure findTreasureAI;
    private FindEnemyFort findEnemyFortAI;
    private int moves = 0;

    public void addPropertyChangeListener(PropertyChangeListener l){
        changes.addPropertyChangeListener(l);
    }

    public WebClient getBaseWebClient() { return baseWebClient; }

    public Game(String serverBaseUrl, String gameID){
        this.baseWebClient = WebClient.builder().baseUrl(serverBaseUrl + "/games/" + gameID)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
                .build();
        this.gameState = new GameState();
        this.gameStateID = gameState.getGameStateId();
        this.player = new Player();
        this.findTreasureAI = new FindTreasure();
        this.findEnemyFortAI = new FindEnemyFort();
    }

    public void registerPlayer(String fName, String lName, String studentID) {
        ConverterPlayerReg playerRegistration = new ConverterPlayerReg(baseWebClient, fName, lName, studentID);
        this.player.setUniquePlayerID(playerRegistration.getPlayerID());
    }
    public String getPlayerID(){ return player.getUniquePlayerID(); }
    public void setCollectedTreasure(boolean collected){ this.player.setCollectedTreasure(collected); }
    public boolean getPlayerFoundTreasure() { return player.collectedTreasure(); }
    public void findPlayerPosition() { player.findMyPosition(gameState.getMap()); }


    public GameState getGameState() { return gameState; }
    public void setGameState(GameState _newGameState){
        String oldGameState = gameState.getGameStateId();
        this.gameState = _newGameState;
        changes.firePropertyChange("gameStateID", oldGameState, gameState.getGameStateId());
    }

    // create HalfMap and send it to Server
    public void sendHalfMap(){
        PlayersMap playersMap = new PlayersMap(player.getUniquePlayerID());
        playersMap.printPlayersMap((Set<HalfMapNode>) playersMap.getPlayersMap().getNodes());
        new ConverterSendHalfMap(baseWebClient, playersMap.getPlayersMap());
    }

    public void setGameMap(){
        this.gameMap = new FullGameMap(gameState.getMap());
    }
    public void printGameMap(){
            this.gameMap.printGameMap(gameState.getMap());
    }

    public void findTreasure(){
        EMove direction = findTreasureAI.myMove(gameState.getMap().get(), player.getPlayerPosition());
        new ConverterSendMove(baseWebClient, PlayerMove.of(player.getUniquePlayerID(), direction));
        logger.info("You've made: " + ++moves + " moves.");
    }

    public void findEnemyFort(){
        EMove direction = findEnemyFortAI.myMove(gameState.getMap().get(), player.getPlayerPosition());
        new ConverterSendMove(baseWebClient, PlayerMove.of(player.getUniquePlayerID(), direction));
        logger.info("You've made: " + ++moves + " moves.");
    }
}
