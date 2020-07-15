package controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import game.Game;

class ControllerTest {
	private String gameID;
	private String url;
	Game testGame;
	Controller testController;
	
	@BeforeEach
	void setUp() throws Exception {
		String gameID = "12345";
		String url = "http://swe.wst.univie.ac.at:18235";
		Game testGame = new Game(url, gameID);
		Controller testController = new Controller(testGame);
	}

	@Test
	void test() {
		assertThrows(NullPointerException.class,
				() -> testController.registerPlayer());
	}

}
