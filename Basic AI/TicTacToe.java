import java.util.*;

/*
   TicTacToe is a class with the main method for setting the game, displaying the board and moves,
   making moves in turn by the user and the AI player, and displaying the final result.
*/
public class TicTacToe {

    public static void main(String[] args) {
        AIplayer AI= new AIplayer(); //an instance/object of the AIplayer class
        Board b = new Board(); //an instance/object of the Board class
        Point p = new Point(0, 0); //an instance/object of the Point class
        
        b.displayBoard(); //Display an empty board

        while (true) {
            System.out.println("Who makes first move? (1)Computer (2)User: ");
            int choice = b.scan.nextInt();
            if (choice == 1) { //AI player starts
                AI.callMinimax(0, 1, b); //Get the values of the possible moves by the AI player through minimax search
                for (PointsAndScores pas : AI.rootsChildrenScores) { //Display these values
                    System.out.println("Point: " + pas.point + " Score: " + pas.score);
                }
                b.placeAMove(AI.returnBestMove(), 1); //AI player makes a move where the value to the AI player is maximum
                b.displayBoard(); //Display the current board
                break;
            }
            if (choice != 2) { //in case they choose a number that isn't 1 or 2
                System.out.println("Please input either a 1 or a 2");
            } else {
                break;
            }
        }
        
        while (!b.isGameOver()) { //If the game is not over yet
            while (true) { //
                try {
                    System.out.println("Your move: line (1, 2, 3, 4, or 5) column (1, 2, 3, 4, or 5)");
                    Point userMove = new Point(b.scan.nextInt() - 1, b.scan.nextInt() - 1); //User chooses a move
                    while (b.getState(userMove) != 0) { //Check whether the move chosen by the user is valid. If invalid, choose again.
                        System.out.println("Invalid move. Make your move again: ");
                        userMove.x = b.scan.nextInt() - 1;
                        userMove.y = b.scan.nextInt() - 1;
                    }
                    b.placeAMove(userMove, 2); //User makes a move
                    b.displayBoard(); //Display the current board
                    break;
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Move index not valid");
                }
            }

            if (b.isGameOver()) { //If the game is over, go to displaying the result
                break;
            }
            
            AI.callMinimax(0, 1, b); //Get the values of the possible moves by the AI player through minimax search
            for (PointsAndScores pas : AI.rootsChildrenScores) { //Display these values
                System.out.println("Point: " + pas.point + " Score: " + pas.score);
            }
            b.placeAMove(AI.returnBestMove(), 1); //AI player makes a move where the value to the AI player is maximum
            b.displayBoard(); //Display the current board
        }
        
        if (b.hasXWon()) { //Get and display the game result
            System.out.println("Unfortunately, you lost!");
        } else if (b.hasOWon()) {
            System.out.println("You win!");
        } else {
            System.out.println("It's a draw!");
        }
    }    
}