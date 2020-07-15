package map;

import MessagesBase.HalfMap;
import MessagesBase.ETerrain;
import MessagesBase.HalfMapNode;
import ai.CheckHalfMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;


public class PlayersMap {
    private final Logger logger = LoggerFactory.getLogger(PlayersMap.class);
    private HalfMap playersMap;
    private CheckHalfMap finalMapCheck;

    public PlayersMap(String playerID){
        long startTime = System.currentTimeMillis();

        Set<HalfMapNode> randomMap;

        // quantity of tiles and their types
        int w = 5;
        int m = 5;
        int g = 22;

        // fill an array with tiles and randomize order
        ArrayList<ETerrain> randomTiles = new ArrayList<>();
            while(w > 0){ randomTiles.add(ETerrain.Water); --w; }
            while(m > 0){ randomTiles.add(ETerrain.Mountain); --m; }
            while(g > 0){ randomTiles.add(ETerrain.Grass); --g; }

        do {
            do{
//                logger.debug("Shuffling tiles...");
                Collections.shuffle(randomTiles);
            } while(!checkWaterTiles(randomTiles));
            randomMap = createMap(randomTiles);
            finalMapCheck = new CheckHalfMap(randomMap);
        } while (!finalMapCheck.floodFill());

        this.playersMap = new HalfMap(playerID, randomMap);

        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        logger.info("Created Players HalfMap in: " + duration + " ms");
    }

    public Set<HalfMapNode> createMap(ArrayList<ETerrain> tiles){
        Set<HalfMapNode> randomNodes = new HashSet<>();
        boolean fort = false;
        int y = 0;	//row
        int x = 0;	//column
        int counterTiles = 0;

        for(ETerrain terra : tiles){
            if(x > 7){
                x = 0;
                ++y;
            }
            if(!fort && counterTiles > 18 && terra == ETerrain.Grass){
                randomNodes.add(new HalfMapNode(x, y, true, terra));
                fort = true;
            } else {
                randomNodes.add(new HalfMapNode(x, y, false, terra));
            }
            ++x;
            ++counterTiles;
        }
        return randomNodes;
    }

    public boolean checkWaterTiles(ArrayList<ETerrain> randomTiles) {
        //check for Water fields
//        logger.debug("Checking Water Tiles...");
        int countWater = 0;
        for(int i = 0; i < 8; ++i){
            if(randomTiles.get(i) == ETerrain.Water) { ++countWater; }
            if(countWater > 3) { return false; }
        }
        countWater = 0;
        for(int i = 24; i < 32; ++i){
            if(randomTiles.get(i) == ETerrain.Water) { ++countWater; }
            if(countWater > 3) { return false; }
        }
        countWater = 0;
        for(int i = 0; i < 25; i+=8){
            if(randomTiles.get(i) == ETerrain.Water) { ++countWater; }
            if(countWater > 1) { return false; }
        }
        countWater = 0;
        for(int i = 7; i < 32; i+=8) {
            if (randomTiles.get(i) == ETerrain.Water) { ++countWater; }
            if (countWater > 1) { return false; }
        }
//        logger.debug("Water Tiles - OK!");
        return true;
    }


    public HalfMap getPlayersMap(){
        return playersMap;
    }

    public void printPlayersMap(Set<HalfMapNode> collection) {
    	if(collection.isEmpty()) {
    		throw new NullPointerException("Can't print empty HalfMap.");
    	}
    	
        int row = 0;
        int column = 0;
        for(int i = 0; i < 8; ++i){ System.out.print("----"); }
        System.out.println("-");
        while(row < 4) {
            while(column < 8) {
                for (Iterator<HalfMapNode> it = collection.iterator(); it.hasNext();) {
                    HalfMapNode f = it.next();
                    if(f.getY() == row && f. getX() == column) {
                        String sign = "";
                        System.out.print("|");
                        switch(f.getTerrain()) {
                            case Grass: sign = "..."; break;
                            case Water: sign = "~~~"; break;
                            case Mountain: sign = "^^^"; break;
                        }
                        if(f.isFortPresent()) {
                            System.out.print("[ ]");
                        } else {
                            System.out.print(sign);
                        }
                    }
                }
                ++column;
            }
            System.out.print("|\n");
            for(int i = 0; i < 8; ++i){ System.out.print("----"); }
            System.out.println("-");
            column = 0;
            ++row;
        }
    }
}
