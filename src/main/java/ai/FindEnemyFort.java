/*------------------------
Algorithm to move around the map was developed based on the information from the following source:
https://gamedev.stackexchange.com/a/55475
 ------------------------*/

package ai;

import MessagesBase.EMove;
import MessagesBase.ETerrain;
import MessagesGameState.FullMap;
import MessagesGameState.FullMapNode;
import map.FullGameMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class FindEnemyFort {
    private static final Logger logger = LoggerFactory.getLogger(FindTreasure.class);

    private Stack<EMove> path = new Stack<>();
    private Map<FullMapNode, Integer> visited = new HashMap<>();

    private FullMapNode destination = null;

    public EMove myMove(FullMap _map, FullMapNode _myPosition){
        if(visited.isEmpty()) { fillVisited(_map, _myPosition); }

        visited.computeIfPresent(_myPosition, (k, v) -> v + 1);
//        logger.debug("Level of current Tile: " + visited.get(_myPosition));
        EMove direction = null;
        Set<FullMapNode> neighbours = lookAround(_map, _myPosition);

        FullMapNode minNeighbour = null;
        for(FullMapNode temp : neighbours) {
//            logger.debug("Checking neighbour: [" + temp.getX() + "," + temp.getY() + "] = " + visited.get(temp));
            if(minNeighbour == null || visited.get(temp) < visited.get(minNeighbour)){
                minNeighbour = temp;
            }
        }

        if(visited.get(minNeighbour) < visited.get(_myPosition)){
            destination = minNeighbour;
            direction = getMove(destination, _myPosition);
//            logger.debug("[" + _myPosition.getX() + "," + _myPosition.getY() + "] => "
//                    + "[" + destination.getX() + "," + destination.getY() + "]");
        }

        if(direction == null) {
            visited.computeIfPresent(_myPosition, (k, v) -> v + 5);
            direction = inverseMove(path.pop());
            logger.info("Going back: " + direction.toString());
            return direction;
        }

        logger.info("Going: " + direction.toString());
        path.push(direction);
        return direction;
    }

    public Set<FullMapNode> lookAround(FullMap _map, FullMapNode _myPosition){
        Set<FullMapNode> neighbours = new HashSet<>();
        for(FullMapNode temp : _map.getMapNodes()){
            if(temp.getTerrain() != ETerrain.Water
                    && Math.abs(temp.getX() - _myPosition.getX()) == 1
                    && temp.getY() - _myPosition.getY() == 0){
                neighbours.add(temp);
            }
            else if(temp.getTerrain() != ETerrain.Water
                    && Math.abs(temp.getY() - _myPosition.getY()) == 1
                    && temp.getX() - _myPosition.getX()== 0){
                neighbours.add(temp);
            }
        }
        return neighbours;
    }

    public EMove getMove(FullMapNode _dest, FullMapNode _currentPos){
        if(_dest.getX() - _currentPos.getX() == -1) { return EMove.Left; }
        if(_dest.getX() - _currentPos.getX() == 1) { return EMove.Right; }
        if(_dest.getY() - _currentPos.getY() == -1) { return EMove.Up; }
        if(_dest.getY() - _currentPos.getY() == 1) { return EMove.Down; }
        else { return null; }
    }

    public EMove inverseMove(EMove _move){
        switch(_move){
            case Left: return EMove.Right;
            case Right: return EMove.Left;
            case Up: return EMove.Down;
            case Down: return EMove.Up;
            default: return null;
        }
    }

    public void fillVisited(FullMap _map, FullMapNode _myPosition){
        //if map is square fill enemy half with high values to block from visiting
        if(FullGameMap.isSquareForm()){
            if(_myPosition.getY() < 4){
                for(FullMapNode temp : _map.getMapNodes()){
                    switch(temp.getY()){
                        case 0: visited.put(temp, 99); break;
                        case 1: visited.put(temp, 89); break;
                        case 2: visited.put(temp, 79); break;
                        case 3: visited.put(temp, 69); break;
                        default:
                            if(temp.getTerrain() == ETerrain.Mountain) {
                                visited.put(temp, 4);
                            } else {
                                visited.put(temp, 0);
                            }                    }
                }
            } else {
                for(FullMapNode temp : _map.getMapNodes()){
                    switch(temp.getY()){
                        case 4: visited.put(temp, 69); break;
                        case 5: visited.put(temp, 79); break;
                        case 6: visited.put(temp, 89); break;
                        case 7: visited.put(temp, 99); break;
                        default:
                            if(temp.getTerrain() == ETerrain.Mountain) {
                                visited.put(temp, 4);
                            } else {
                                visited.put(temp, 0);
                            }                    }
                }
            }
        } else {
            if(_myPosition.getX() < 8){
                for(FullMapNode temp : _map.getMapNodes()){
                    switch(temp.getX()){
                        case 0: visited.put(temp, 100); break;
                        case 1: visited.put(temp, 98); break;
                        case 2: visited.put(temp, 96); break;
                        case 3: visited.put(temp, 94); break;
                        case 4: visited.put(temp, 92); break;
                        case 5: visited.put(temp, 90); break;
                        case 6: visited.put(temp, 88); break;
                        case 7: visited.put(temp, 86); break;
                        default:
                            if(temp.getTerrain() == ETerrain.Mountain) {
                                visited.put(temp, 4);
                            } else {
                                visited.put(temp, 0);
                            }
                    }
                }
            } else {
                for(FullMapNode temp : _map.getMapNodes()){
                    switch(temp.getX()){
                        case 8: visited.put(temp, 86); break;
                        case 9: visited.put(temp, 88); break;
                        case 10: visited.put(temp, 90); break;
                        case 11: visited.put(temp, 92); break;
                        case 12: visited.put(temp, 94); break;
                        case 13: visited.put(temp, 96); break;
                        case 14: visited.put(temp, 98); break;
                        case 15: visited.put(temp, 100); break;
                        default:
                            if(temp.getTerrain() == ETerrain.Mountain) {
                                visited.put(temp, 4);
                            } else {
                                visited.put(temp, 0);
                            }                    }
                }
            }
        }
    }
}
