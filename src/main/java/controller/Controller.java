package controller;

import game.Game;
import MessagesGameState.EPlayerGameState;
import MessagesGameState.PlayerState;
import converter.ConverterGameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static MessagesGameState.EPlayerGameState.ShouldWait;

public class Controller {
    private final Logger logger = LoggerFactory.getLogger(Controller.class);
    public final static long requestDelay = 250;	//time delay between sending requests to server
    private final Game game;

    public Controller(Game _game){
        this.game = _game;
    }

    public void registerPlayer(){
        logger.info("Player Registration in progress.");
        try{
            this.game.registerPlayer("Andrii", "Nykyforuk", "01247560");
            logger.info("Waiting to act next.");
        } catch(Exception e){
            logger.error("Game Client has ended.");
            System.exit(0);
        }
    }

    public void sendHalfMap() throws InterruptedException {
        logger.info("Creating and sending HalfMap to Server in progress.");
        try{
            this.game.sendHalfMap();
        } catch(Exception e){
            logger.error("Error during sending the HalfMap: " + e);
        }
    }

    // checks GameState from Server and saves the new one in local variable
    public void checkGameState() throws InterruptedException {
        String oldGameState = game.getGameState().getGameStateId();
        do {
            ConverterGameState gameState = new ConverterGameState(game.getBaseWebClient(), game.getPlayerID());
            if(oldGameState != gameState.getGameState().getGameStateId()){
                game.setGameState(gameState.getGameState());
            }
            Thread.sleep(requestDelay);
        } while (checkEPlayerState() == ShouldWait);
    }

    // checks PlayerState: Won, Lost, ShouldWait or ShouldActNext
    public EPlayerGameState checkEPlayerState() {
        Set<PlayerState> tempPlayers = game.getGameState().getPlayers();
        PlayerState tempState = null;
        for(PlayerState p : tempPlayers) {
            if(p.getUniquePlayerID().contains(game.getPlayerID())) {
//                logger.debug("PlayerID: " + p.getUniquePlayerID() + " | State: " + p.getState());
                tempState = p;
                break;
            }
        }
        game.setCollectedTreasure(tempState.hasCollectedTreasure());
        return tempState.getState();
    }

    public void findPlayersMap(){
        logger.info("Check if FullMap is available...");
        try{
            this.game.setGameMap();	//set size of the map so it can be printed out
            logger.info("FullMap is available - OK!");
            printGameMap();
        } catch(Exception e){
            logger.error("Error during checking for FullMap: " + e);
        }
    }
    public void printGameMap() { this.game.printGameMap(); }

    public void playGame() throws InterruptedException {
        for(;;) {
            switch (checkEPlayerState()) {
                case ShouldWait:
                    logger.info("You should wait.");
                    checkGameState();
                    break;
                case ShouldActNext:
                    game.findPlayerPosition();
                    if(game.getPlayerFoundTreasure()) {
                        game.findEnemyFort();
                    } else {
                        game.findTreasure();
                    }
                    checkGameState();
                    break;
                case Won:
                    logger.info("Congratulations! You have won. " + game.getPlayerID());
                    logger.info("Game Client has ended.");
                    System.exit(0);
                case Lost:
                    logger.info("Sorry but you have lost. " + game.getPlayerID());
                    logger.info("Game Client has ended.");
                    System.exit(0);
                default:
                    logger.error("Oops! Something went wrong.");
                    logger.error("Game Client has ended.");
                    System.exit(1);
            }
        }
    }
}
