package map;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import MessagesBase.HalfMapNode;

class PlayersMapTest {
	private final PlayersMap testMap = new PlayersMap("01234567");
		 
	@Test
	void testIfMinTilesPresent() {
		int mountainTiles = 0;
		int waterTiles = 0;
		int grassTiles = 0;
		for(HalfMapNode temp : testMap.getPlayersMap().getNodes()) {
			if(temp.getTerrain() == ETerrain.Water) {
				waterTiles++;
			}
			if(temp.getTerrain() == ETerrain.Grass) {
				grassTiles++;
			}
			if(temp.getTerrain() == ETerrain.Mountain) {
				mountainTiles++;
			}
		}
		assertTrue("There is enough Water Tiles.", waterTiles > 4);
		assertTrue("There is enough Grass Tiles.", grassTiles > 15);
		assertTrue("There is enough Mountain Tiles.", mountainTiles > 3);
	}

	@Test
	void testGetPlayersMap() {
		assertNotNull("Player Map is present", testMap.getPlayersMap());
	}
	
	@Test
	void print_emptyMap_shouldThrow_Exception() {
		Set<HalfMapNode> emptyHalfMap = (Set<HalfMapNode>) new HalfMap().getNodes();
		
		assertThrows(NullPointerException.class,
				() -> testMap.printPlayersMap(emptyHalfMap));
	}
}
