package game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GameTest {
	private Game testGame;
	
	@BeforeEach
	void setUp() {
		testGame = Mockito.mock(Game.class);
	}
	
	@Test
	void registerPlayer_must_receive_UniqueID_or_Error() {
		String fName = "Andrii";
		String lName = "Nykyforuk";
		String studentID = "01247560";
			
		testGame.registerPlayer(fName, lName, studentID);
//		Mockito.verify(testGame).registerPlayer(fName, lName, studentID);
	}
	
	@Test
	void findPlayerPosition_must_return_FullMapNode() {
		testGame.findPlayerPosition();
		Mockito.verify(testGame).findPlayerPosition();
	}

}
