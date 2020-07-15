package game;

import MessagesGameState.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Set;

public class Player {
    private final Logger logger = LoggerFactory.getLogger(Player.class);

    private String uniquePlayerID;
    private boolean collectedTreasure = false;
    private FullMapNode playerPosition;

    public String getUniquePlayerID() { return uniquePlayerID; }
    public void setUniquePlayerID(String uniquePlayerID) { this.uniquePlayerID = uniquePlayerID; }

    public boolean collectedTreasure() { return collectedTreasure; }
    public void setCollectedTreasure(boolean collected) {
        if(this.collectedTreasure != collected){
            this.collectedTreasure = collected;
            logger.info("Congrats! You have found the treasure.");
        }
    }

    public FullMapNode getPlayerPosition(){ return playerPosition; }
    public void findMyPosition(Optional<FullMap> fullMap){
        Set<FullMapNode> gameMapNodes = fullMap.get().getMapNodes();
        for (FullMapNode f : gameMapNodes) {
            if(f.getPlayerPositionState() == EPlayerPositionState.MyPosition
                    || f.getPlayerPositionState() == EPlayerPositionState.BothPlayerPosition){
                logger.info("Player Position: [" + f.getX() + "," + f.getY() + "]");
                this.playerPosition = f;
            }
        }
    }
}
