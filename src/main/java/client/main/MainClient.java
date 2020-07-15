package client.main;

import controller.Controller;
import game.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viewer.ViewCLI;

public class MainClient {
	private static final Logger logger = LoggerFactory.getLogger(MainClient.class);

	public static void main(String[] args) throws InterruptedException {
		logger.info("Game Client has started.");

		/* --- Begin experiments with Path finding --- */

		/* --- End experiments with Path finding --- */


		/* --- GameMode TR - Tournament or DR - for debugging --- */

//		String gameMode = args[0];
//		String serverBaseUrl = args[1];
//		String gameID = args[2];
		String serverBaseUrl = "http://localhost:18235";
		String gameID = "2ugMM";

		Game game = new Game(serverBaseUrl, gameID);
		Controller controller = new Controller(game);
		ViewCLI viewCLI = new ViewCLI(game, controller);

		// Register Player and wait to act next
		controller.registerPlayer();
		controller.checkGameState();

		// Create and send HalfMap, wait to act next
		controller.sendHalfMap();
		controller.checkGameState();

		//Play the game
		controller.findPlayersMap();
		controller.playGame();

		logger.info("Game Client has ended.");
	}
}
