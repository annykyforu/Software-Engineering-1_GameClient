package map;

import MessagesBase.ETerrain;
import MessagesGameState.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public class FullGameMap {
    private static final Logger logger = LoggerFactory.getLogger(FullGameMap.class);

    private static boolean square = false;
    private static int maxX;
    private static int maxY;

    private static int playerMinX, playerMaxX;
    private static int playerMinY, playerMaxY;

    public FullGameMap(Optional<FullMap> wholeMap){
        Set<FullMapNode> gameMapNodes = wholeMap.get().getMapNodes();
        int _maxX = 0;
        int _maxY = 0;
        for(FullMapNode tempNode: gameMapNodes) {
            if(tempNode.getX() > _maxX) { _maxX = tempNode.getX(); }
            if(tempNode.getY() > _maxY) { _maxY = tempNode.getY(); }
        }
        maxX = _maxX;
        maxY = _maxY;
        if(maxX == 7 && maxY == 7) {
//            logger.debug("This map is 8x8.");
            square = true;
        } else {
//            logger.debug("This map is 16x4.");
        }
        setMapPart(wholeMap);
    }

    public static int getMaxX() { return maxX; }
    public static int getMaxY() { return maxY; }
    public static boolean isSquareForm() { return square; }

    //define the coordinates of this Players part of the Map
    public void setMapPart(Optional<FullMap> wholeMap){
        FullMapNode node = new FullMapNode();
        for(FullMapNode tempNode : wholeMap.get().getMapNodes()){
            if(tempNode.getFortState() == EFortState.MyFortPresent) {
                node = tempNode;
                break;
            }
        }
//        logger.debug("Fort is here: [" + node.getX() + "," + node.getY() + "]");
        if(square){
            playerMinX = 0; playerMaxX = 7;
            if(node.getY() < 4) {    //if players Map is above
                playerMinY = 0; playerMaxY = 3;
                logger.debug("My map is above.");
            } else {                //if players Map is below
                playerMinY = 4; playerMaxY = 7;
                logger.debug("My map is below.");
            }
        } else {
            playerMinY = 0; playerMaxY = 3;
            if(node.getX() < 8) {    //if players Map is left
                playerMinX = 0; playerMaxX = 7;
                logger.debug("My map is left.");
            } else {                //if players Map is right
                playerMinX = 8; playerMaxX = 15;
                logger.debug("My map is right.");
            }
        }
    }

    public void printGameMap(Optional<FullMap> wholeMap) {
        int columnX = 0;
        int rowY = 0;
        Set<FullMapNode> gameMapNodes = wholeMap.get().getMapNodes();
        for(int i = 0; i <= getMaxX(); ++i){ System.out.print("----"); }
        System.out.println("-");
        while(rowY <= getMaxY()) {
            while(columnX <= getMaxX()) {
                for (Iterator<FullMapNode> it = gameMapNodes.iterator(); it.hasNext();) {
                    FullMapNode f = it.next();
                    if(f.getY() == rowY && f. getX() == columnX) {
                        String sign = "";
                        System.out.print("|");
                        switch(f.getTerrain()) {
                            case Grass: sign = "..."; break;
                            case Water: sign = "~~~"; break;
                            case Mountain: sign = "^^^"; break;
                        }
                        switch(f.getFortState()) {
                            case MyFortPresent: sign = "[ ]"; break;
                            case EnemyFortPresent: sign = "{ }"; break;
                            default: break;
                        }
                        switch(f.getPlayerPositionState()) {
                            case MyPosition: sign = ".X."; break;
                            case EnemyPlayerPosition: sign = ".@."; break;
                            case BothPlayerPosition: sign = "X.@"; break;
                            default: break;
                        }
                        switch(f.getTreasureState()) {
                            case MyTreasureIsPresent: sign = ".$."; break;
                            default: break;
                        }
                        if(f.getPlayerPositionState() == EPlayerPositionState.MyPosition
                                && (f.getTerrain() == ETerrain.Mountain)){
                            sign = "^X^";
                        }
                        if(f.getFortState() == EFortState.MyFortPresent
                                && f.getPlayerPositionState() == EPlayerPositionState.MyPosition) {
                            sign = "[X]";
                        }
                        if(f.getFortState() == EFortState.EnemyFortPresent
                                && f.getPlayerPositionState() == EPlayerPositionState.MyPosition) {
                            sign = "{X}";
                        }
                        if(f.getFortState() == EFortState.MyFortPresent
                                && f.getPlayerPositionState() == EPlayerPositionState.EnemyPlayerPosition) {
                            sign = "[@]";
                        }
                        if(f.getFortState() == EFortState.EnemyFortPresent
                                && f.getPlayerPositionState() == EPlayerPositionState.EnemyPlayerPosition) {
                            sign = "{@}";
                        }
                        System.out.print(sign);
                    }
                }
                ++columnX;
            }
            System.out.print("|\n");
            columnX = 0;
            ++rowY;
            for(int i = 0; i <= getMaxX(); ++i){ System.out.print("----"); }
            System.out.println("-");
        }
    }
}
