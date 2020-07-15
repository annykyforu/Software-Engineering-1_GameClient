package viewer;

import java.beans.PropertyChangeListener;

import controller.Controller;
import game.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewCLI {
    private final Logger logger = LoggerFactory.getLogger(ViewCLI.class);
    private final Controller controller;

    public ViewCLI(Game game, Controller _controller){
        this.controller = _controller;
        game.addPropertyChangeListener(gameChangedListener);
    }

    // used to check Game State ID
    final PropertyChangeListener gameChangedListener = event -> {
        Object newGameStateID = event.getNewValue();
        logger.info("New Game State: " + newGameStateID);
        try {
            this.printGameMap();
        } catch (NullPointerException e){
            logger.info("Game Map is not yet available.");
        }
    };

    public void printGameMap(){ this.controller.printGameMap(); }
}
