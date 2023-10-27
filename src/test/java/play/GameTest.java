package play;

import checkers.Game;
import org.junit.Test;

public class GameTest {
    @Test
    public void testFiveMovesGame() {
        //initialize game and board
        var game = new Game();
        var board = game.getBoard();
        //define messages for successful move
        String[] messages = {"move", "Select"};
        //create 2-dimensional array for checker moves
        int[][] moves = {
                {5, 1, 4, 0},
                {6, 2, 5, 1},
                {7, 3, 6, 2},
                {5, 5, 4, 6},
                {5, 3, 3, 5}
        };

        int moveCount = 0;
//while loop for 5 moves
        while (moveCount < 5 && game.isMessageAllowsToMove(messages[0])) {

            int[] moveCoordinates = moves[moveCount];
            board.makeMove(moveCoordinates[0], moveCoordinates[1], moveCoordinates[2], moveCoordinates[3]);
            board.assertSpotIsOccupied(moveCoordinates[2], moveCoordinates[3]);
            moveCount++;
        }
// restart game
        game.restart();
//check message for first move is displayed
        assert (game.isMessageAllowsToMove(messages[1]));
    }
}