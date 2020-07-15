/*------------------------
Algorithm to check the HalfMap was developed based on the information from the following sources:
1. https://www.hackerearth.com/practice/algorithms/graphs/flood-fill-algorithm/tutorial/
2. MAZE EXPLORATION ALGORITHM FOR SMALL MOBILE PLATFORMS - DOI: 10.1515/ipc-2016-0013
3. https://en.wikipedia.org/wiki/Flood_fill
 ------------------------*/

package ai;

import MessagesBase.ETerrain;
import MessagesBase.HalfMapNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class CheckHalfMap {
    private static final Logger logger = LoggerFactory.getLogger(CheckHalfMap.class);

    private Set<HalfMapNode> unvisited; //all nodes of the map
    private Set<HalfMapNode> unchecked; //nodes of neighbours to be checked
    private HalfMapNode currentStart;
    private int visitedTiles;

    public CheckHalfMap(Set<HalfMapNode> halfMap){
        this.unchecked = new HashSet<>();
        this.unvisited = new HashSet<>();
        for(HalfMapNode node: halfMap){
            if(node.getTerrain() == ETerrain.Grass){
                this.unvisited.add(node);
            }
            if(node.isFortPresent()){
                this.currentStart = node;
            }
        }
        this.visitedTiles = 1;
    }

    public boolean floodFill(){
        do{
//            logger.debug("Checking neighbours of: [" + currentStart.getX() + "," + currentStart.getY() + "]");
            checkNeighbours(currentStart);
            visitedTiles++;

            removeFromUnvisited(currentStart);
            for(HalfMapNode temp : unchecked){
                removeFromUnvisited(temp);
            }

            if(unchecked.size() != 0) {
                currentStart = unchecked.iterator().next();
            }
            unchecked.remove(currentStart);
        } while(unchecked.size() != 0);

        if(unvisited.size() == 0) {
            logger.debug("HalfMap is OK!");
//            logger.debug("Visited: " + visitedTiles);
            return true;
        } else {
//            logger.debug("Found unreachable tiles. The map will be rebuild.");
            return false;
        }
    }

    public void checkNeighbours(HalfMapNode current){
        for(HalfMapNode neighbour : getUnvisited()){
            if(Math.abs(neighbour.getX() - current.getX()) == 1
                    && neighbour.getY() - current.getY() == 0){
                if(neighbour.getTerrain() == ETerrain.Grass) {
//                    logger.debug("Neighbour: [" + neighbour.getX() + "," + neighbour.getY() + "]");
                    this.unchecked.add(neighbour);
                }
            } else if(neighbour.getX() - current.getX() == 0
                    && Math.abs(neighbour.getY() - current.getY()) == 1){
                if(neighbour.getTerrain() == ETerrain.Grass) {
//                    logger.debug("Neighbour: [" + neighbour.getX() + "," + neighbour.getY() + "]");
                    this.unchecked.add(neighbour);
                }
            }
        }
    }

    public void removeFromUnvisited(HalfMapNode node){
        if(unvisited.remove(node)){
//            logger.debug("Removed node from unvisited: [" + node.getX() + "," + node.getY() + "]");
        }
    }

    public Set<HalfMapNode> getUnvisited(){
        return unvisited;
    }
}
