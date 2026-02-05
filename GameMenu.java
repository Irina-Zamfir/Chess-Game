package TEMA1;

import java.util.*;

import TEMA1.Exceptions.InvalidCommandException;
import TEMA1.Exceptions.InvalidMoveException;
import TEMA1.Exceptions.PromotionNeededException;
import TEMA1.GAME.*;
import TEMA1.GAME.Pieces.Colors;
import TEMA1.GAME.Pieces.Piece;
import TEMA1.GAME.Pieces.Position;
import TEMA1.User.User;

public class GameMenu {

    private User currentUser;
    private Game game;
    private Player playerUser;
    private Player computer;

    public GameMenu(User currentUser, Game game, Player playerUser, Player computer) {
        this.currentUser = currentUser;
        this.game = game;
        this.playerUser = playerUser;
        this.computer = computer;
    }

    public Game getGame() {
        return this.game;
    }

    public Player getPlayerUser() {
        return this.playerUser;
    }

    public Map<Piece, List<Position>> getAllComputerMoves(Board board, Player computer) {
        List<ChessPair<Position, Piece>> getComputerPieces = computer.getOwnedPieces(board);

        // iterate through all the moves and add to a bigger list of moves
        Map<Piece, List<Position>> movesPieceAndPosition = new HashMap<>();

        for (ChessPair<Position, Piece> pair : getComputerPieces) {
            Piece currentPiece = pair.getValue();
            List<Position> movesForCurrentPiece = currentPiece.getPossibleMoves(board);
            movesPieceAndPosition.put(currentPiece, movesForCurrentPiece);
        }

        return movesPieceAndPosition;
    }

    public boolean makeComputerMove() throws InvalidMoveException {

        if (game.getChessBoard().isKingInCheck(game.getCurrentPlayer().getColor())) {
            System.out.println("You are in check!");
        }

        System.out.print("Introduce a move in the limits of the board: ");

        Board board = game.getChessBoard();
        Map<Piece, List<Position>> computerMoves = getAllComputerMoves(board, computer);
        List<Piece> keys = new ArrayList<>(computerMoves.keySet());
        Random random = new Random();

        int nrKeys = keys.size();
        boolean moveWasMade = false;

        while (!moveWasMade) {
            Piece randomKey = keys.get(random.nextInt(nrKeys));
            Position from = randomKey.getPosition();

            List<Position> positions = new ArrayList<>(computerMoves.get(randomKey));
            Piece pieceComputerWantsToMove = game.getChessBoard().getPieceAt(from);

            // keeps checking if the move is valid and only does the move when it is valid
            // it will have at list a valid move because this method is called when there is not checkmate
            for (Position to : positions) {
                if (board.isValidMove(from, to)) {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    Piece capturedPiece = null;
                    try {
                        System.out.println(from + "-" + to);
                        capturedPiece = computer.makeMove(from ,to, game.getChessBoard());

                    } catch (PromotionNeededException e) {
                        System.out.println(from + "-" + to);
                        System.out.println("Computer promotes pawn to Queen (Q)");

                        game.getChessBoard().applyPromotion(to, computer.getColor(), 'Q');
                    } finally {
                        game.addMove(computer, from, to, capturedPiece, pieceComputerWantsToMove.type());
                        moveWasMade = true;
                    }

                    Move moveJustMade = new Move(computer.getColor(), from, to, capturedPiece, pieceComputerWantsToMove.type());
                    if (game.threeMovesMadeTheSame(moveJustMade)) {
                        return false;
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    return true;
                }
            }
        }

        return false;
    }

    public boolean checkMoveCommand(String move) {
        if(move.length() > 5 || move.length() < 2)
            return false;

        if(move.charAt(0) < 'A' || move.charAt(0) > 'H')
            return false;

        if(move.charAt(1) < '1' || move.charAt(1) > '8')
            return false;

        if (move.length() > 2) {
            if(move.charAt(3) < 'A' || move.charAt(3) > 'H')
                return false;

            if(move.charAt(4) < '1' || move.charAt(4) > '8')
                return false;
        }

        return true;
    }

    public char getPieceThatPlayerWants(Scanner s) {
        char chosenType;
        boolean isValid = false;

        while (!isValid) {
            System.out.print("The pawn promoted! Choose a piece (Q, R, B, N): ");
            String input = s.nextLine().toUpperCase().trim();

            if (input.length() == 1) {
                chosenType = input.charAt(0);
                if (chosenType == 'Q' || chosenType == 'R' || chosenType == 'B' || chosenType == 'N') {
                    isValid = true;
                    return chosenType;
                }
            }

            System.out.println("Error: The chosen piece is not valid. Please try again.");
        }

        return 'Q';
    }

    public boolean makePlayerMove(Scanner s)
            throws InvalidCommandException {

        if (game.getChessBoard().isKingInCheck(game.getCurrentPlayer().getColor())) {
            System.out.println("You are in check!");
        }

        while (true) {
            System.out.print("Introduce a move in the limits of the table: ");
            String move = s.nextLine().trim();

            // pauses game and then it should proceed to saving it
            if (move.equalsIgnoreCase("pause game")) {
                return false;
            }

            try {
                if (!checkMoveCommand(move))
                    throw new InvalidCommandException("Invalid Command");
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
                continue;
            }

            /**
             *
             * if (move.length() == 2) {
             *      call method printAllPossibleMoves();
             *
             *      // afiseaza toate miscarile pt piesa de la pozitia data
             *      si da continue ca sa faca an actual move
             *
             * } else if (move.length() == 5) {
             *      call method executectual move();
             * }
             *
             */

            if (move.length() < 5) {

                Position posForPieceToGetMoves = new Position(move.charAt(0), move.charAt(1) - '0');
                Piece pieceToGetAllMovesFor = game.getChessBoard().getPieceAt(posForPieceToGetMoves);

                if (pieceToGetAllMovesFor == null) {
                    System.out.println("There is no piece on that position");
                } else {
                    View.printAllPossibleMoves(game.getChessBoard(), pieceToGetAllMovesFor);
                }

                continue;
            }

            Position from = new Position(move.charAt(0), move.charAt(1)-'0');
            Position to = new Position(move.charAt(3), move.charAt(4)-'0');
            Piece piecePlayerWantsToMove = game.getChessBoard().getPieceAt(from);

            Piece capturedPiece = null;
            try {
                capturedPiece = playerUser.makeMove(from, to, game.getChessBoard());
                game.addMove(playerUser, from, to, capturedPiece, piecePlayerWantsToMove.type());
                return true;
            } catch (PromotionNeededException e) {
                try {
                    char chosenType = getPieceThatPlayerWants(s);
                    game.getChessBoard().applyPromotion(to, playerUser.getColor(), chosenType);
                    capturedPiece = e.getCapturedPiece();
                    game.addMove(playerUser, from, to, capturedPiece, piecePlayerWantsToMove.type());
                    return true;
                } catch (IllegalArgumentException | NoSuchElementException ex) {
                    System.out.println("Error: The piece you introduced is invalid. Retry the move.");
                }
            } catch(InvalidMoveException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public void runGame(Scanner s) throws InvalidMoveException {

        System.out.println("\n--- Start Game ---");
        System.out.println("""
                Remember:\s
                 - If you get bored or for any other reason you want to stop the game
                just type in 'Pause game'\s""");

        System.out.println("\n Are you ready to start the game? (YES/NO)");
        System.out.print("Choice: ");
        String userInput = s.nextLine();
        if (userInput.equalsIgnoreCase("no")) {
            return;
        }

        System.out.println("\nAlright ! Let's get started !!");
        System.out.println("3");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("2");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("1...");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        View.printBoard(game.getChessBoard());

        while (true) {

            Player currentPlayer = game.getCurrentPlayer();
            Player opponent = (currentPlayer == game.getWhitePlayer()) ? game.getBlackPlayer() : game.getWhitePlayer();
            boolean continueGamePlayer = true;
            boolean continueGameComputer = true;

            if (game.checkForCheckMate()) {
                System.out.println("-------------------------------------");
                System.out.println("--- GAME OVER! Check Mate ---");
                System.out.println("-------------------------------------");

                game.awardGamePoints(opponent, Game.GameEndReason.CHECKMATE);
                currentUser.removeGame(game);
                break;
            }

            if (currentPlayer.equals(playerUser)) {
                System.out.println(currentPlayer.getColor() + " Player's Turn");
                if(!makePlayerMove(s)) {
                    continueGamePlayer = false;
                }
            } else {
                System.out.println(currentPlayer.getColor() + " Player's Turn");
                if(!makeComputerMove()) {
                    continueGameComputer = false;
                }
            }

            if (!continueGamePlayer) {
                System.out.println("The game was paused");

                System.out.println("Do you want to continue the game? (YES/NO/DRAW/FORFEIT)");
                String message = s.nextLine();
                if (message.equalsIgnoreCase("yes")) {
                    continue;
                } else if (message.equalsIgnoreCase("no")) {
                    System.out.println("The game was saved. Going back to the Main Menu");
                    break;
                } else if (message.equalsIgnoreCase("draw")) {
                    System.out.println("Game ended in draw. Removing game from list...");
                    game.awardGamePoints(null, Game.GameEndReason.DRAW);
                    currentUser.removeGame(game);
                    break;
                } else if (message.equalsIgnoreCase("forfeit")) {
                    game.awardGamePoints(computer, Game.GameEndReason.FORFEIT);
                    currentUser.removeGame(game);
                    break;
                } else {
                    System.out.println("The command you introduced is incorrect. The game will now stop");
                    break;
                }
            }

            if (!continueGameComputer) {
                System.out.println("Computer made the same move three times.");
                System.out.println("Computer forfeits the game.");
                game.awardGamePoints(playerUser, Game.GameEndReason.FORFEIT);
                currentUser.removeGame(game);
                break;
            }

            View.printBoard(game.getChessBoard());
            game.switchPlayer();
        }
    }
}
