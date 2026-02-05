package TEMA1;

import TEMA1.GAME.*;
import TEMA1.GAME.Pieces.Colors;
import TEMA1.GAME.Pieces.Piece;
import TEMA1.GAME.Pieces.Position;
import TEMA1.User.User;

import java.util.List;
import java.util.TreeSet;

public class View {

    public static void printAllPossibleMoves(Board board, Piece piece) {
        List<Position> positionList = piece.getPossibleMoves(board);

        for (Position pos : positionList) {
            System.out.print(pos + " ");
        }

        System.out.println();
    }

    public static void printBoard(Board board) {
        TreeSet<ChessPair<Position, Piece>> chessBoard = board.getBoardPieces();

        String[][] actualBoard = new String[8][8];
        for (ChessPair<Position, Piece> pair : chessBoard) {
            Piece piece = pair.getValue();
            Position pos = pair.getKey();

            char type = piece.type();

            char color;
            if (piece.getColor() == Colors.WHITE) {
                color = 'W';
            } else {
                color = 'B';
            }

            char x = pos.getX();
            int y = pos.getY();

            actualBoard[8 - y][((int)(x - 'A'))] = type + "-" + color;
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(actualBoard[i][j] == null) {
                    actualBoard[i][j] = "...";
                }
            }
        }

        // now let s actually print the board
        String bara = "  " + "-".repeat(35);
        System.out.println(bara);

        for(int i = 0 ; i < 8; i++){
            System.out.print(8-i + " | ");

            for(int j = 0; j < 8; j++){
                System.out.print(actualBoard[i][j] + " ");
            }

            System.out.println("|");
        }

        System.out.println(bara);
        System.out.println("     A   B   C   D   E   F   G   H");
    }

    public static void printGameState(Game gameToBeResumed, User currentUser) {

        System.out.println("=========================================");
        System.out.println("                GAME STATE         ");
        System.out.println("=========================================");

        System.out.println("\n GameId: " + gameToBeResumed.getGameId());
        View.printBoard(gameToBeResumed.getChessBoard());

        Player white = gameToBeResumed.getWhitePlayer();
        Player black = gameToBeResumed.getBlackPlayer();

        System.out.println("--- Players ---");
        if (white.getName().equalsIgnoreCase(currentUser.getUserEmail())) {
            System.out.println("WHITE Player: " + currentUser.getUserEmail());
            System.out.println("BLACK Player: computer");
        } else {
            System.out.println("WHITE Player: computer");
            System.out.println("BLACK Player: " + currentUser.getUserEmail());
        }

        System.out.println("Next turn: " + gameToBeResumed.getCurrentPlayer().getColor() + " Player");

        System.out.println("Captured Pieces: ");

        List<Piece> capturedByWhite = white.getCapturedPieces();
        if (!capturedByWhite.isEmpty()) {
            System.out.print("WHITE captured (Total points: " + white.getPoints() + "): ");
            StringBuilder capturedWhiteStr = new StringBuilder();
            for (Piece p : capturedByWhite) {
                capturedWhiteStr.append(p.type()).append("-").append(p.getColor().name().charAt(0)).append(", ");
            }

            capturedWhiteStr.setLength(capturedWhiteStr.length() - 2);
            System.out.println(capturedWhiteStr.toString());
        } else {
            System.out.println("WHITE didn't capture any pieces yet.");
        }

        List<Piece> capturedByBlack = black.getCapturedPieces();
        if (!capturedByBlack.isEmpty()) {
            System.out.print("BLACK captured (Total value: " + black.getPoints() + "): ");
            StringBuilder capturedBlackStr = new StringBuilder();
            for (Piece p : capturedByBlack) {
                capturedBlackStr.append(p.type()).append("-").append(p.getColor().name().charAt(0)).append(", ");
            }
            capturedBlackStr.setLength(capturedBlackStr.length() - 2);
            System.out.println(capturedBlackStr.toString());
        } else {
            System.out.println("BLACK didn't capture any pieces yet.");
        }


        System.out.println("\n--- LIST OF MOVES ---");
        List<Move> allMoves = gameToBeResumed.getGameMoves();

        if (!allMoves.isEmpty()) {
            for (int i = 0; i < allMoves.size(); i++) {
                Move currentMove = allMoves.get(i);

                StringBuilder moveLine = new StringBuilder();
                moveLine.append(i + 1).append(". ");
                moveLine.append(currentMove.getPlayerColor().name()).append(" ");
                moveLine.append(currentMove.getFrom()).append("-").append(currentMove.getTo());

                if (currentMove.getCapturedPiece() != null) {
                    moveLine.append(" [x").append(currentMove.getCapturedPiece().type()).append("]");
                }

                System.out.println(moveLine.toString());
            }

        } else {
            System.out.println("Game didn't start yet (moves: 0).");
        }

        System.out.println("=========================================");

    }
}
