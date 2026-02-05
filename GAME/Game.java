package TEMA1.GAME;

import TEMA1.GAME.Pieces.Colors;
import TEMA1.GAME.Pieces.Piece;
import TEMA1.GAME.Pieces.Position;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private long gameId;
    private Board chessBoard;
    private Player whitePlayer; // human
    private Player blackPlayer; // computer
    private List<Move> gameMoves;
    private Player currentPlayer;

    public Game(long id, Player userPlayer, Player computerPlayer) {
        this.gameId = id;
        this.chessBoard = new Board();
        this.whitePlayer = userPlayer;
        this.blackPlayer = computerPlayer;
        this.gameMoves = new ArrayList<>();
        this.currentPlayer = new Player("mimi", Colors.WHITE);
    }

    public Board getChessBoard() {
        return this.chessBoard;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public Player getWhitePlayer() {
        return this.whitePlayer;
    }

    public Player getBlackPlayer() {
        return this.blackPlayer;
    }

    public List<Move> getGameMoves() {
        return this.gameMoves;
    }

    public long getGameId() {
        return this.gameId;
    }

    // overwrites the list of moves with the moves from an older still active game
    public void setGameMoves(List<Move> movesFromExternalFile) {
        this.gameMoves = movesFromExternalFile;
    }

    public void setCurrentPlayerColor(Colors newColor) {
        if (this.whitePlayer.getColor() == newColor) {
            this.currentPlayer = this.whitePlayer;
        } else if (this.blackPlayer.getColor() == newColor) {
            this.currentPlayer = this.blackPlayer;
        }
    }

    public void start() {
        this.chessBoard = new Board();
        this.chessBoard.initialize();
        this.gameMoves.clear();

        if (whitePlayer.getColor() == Colors.WHITE) {
            this.currentPlayer = whitePlayer;
        } else {
            this.currentPlayer = blackPlayer;
        }
    }

    public void resume() {
        Colors loadedColor = this.getCurrentPlayer().getColor();

        if (this.whitePlayer.getColor() == loadedColor) {
            this.currentPlayer = this.whitePlayer;
        } else if (this.blackPlayer.getColor() == loadedColor) {
            this.currentPlayer = this.blackPlayer;
        } else {
            System.err.println("Error: The current Player's color is not the same as the set players'.");
        }

        System.out.println("Game with ID " + this.gameId + " was initialized to be resumed.");
    }

    public void switchPlayer() {
        if (this.currentPlayer == whitePlayer) {
            this.currentPlayer = blackPlayer;
        } else {
            this.currentPlayer = whitePlayer;
        }
    }

    public boolean checkForCheckMate() {
        Colors playerColor = this.currentPlayer.getColor();

        if (!this.chessBoard.isKingInCheck(playerColor)) {
            return false;
        }

        // iterate through all the players pieces and all of their moves
        List<ChessPair<Position, Piece>> playerPieces = this.currentPlayer.getOwnedPieces(this.chessBoard);
        for(ChessPair<Position, Piece> pair : playerPieces) {
            // get all possible moves for said pair
            // verify for all the moves (with position in for if the move is valid - if it is valid return false)

            Piece currentPiece = pair.getValue();
            Position from = pair.getKey();
            List<Position> moves = currentPiece.getPossibleMoves(this.chessBoard);
            for(Position to : moves) {
                if (this.chessBoard.isValidMove(from, to)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void addMove(Player p, Position from, Position to, Piece capturedPiece, char type) {
        Move move = new Move(p.getColor(), from, to, capturedPiece, type);
        gameMoves.add(move);
    }

    public boolean threeMovesMadeTheSame(Move move) {

        int cnt = 0;
        for (Move m : gameMoves) {
            if (m.equals(move)) {
                cnt++;
            }
        }

        return cnt >= 3;
    }

    public enum GameEndReason {
        CHECKMATE, FORFEIT, DRAW, PAUSE
    }

    public void awardGamePoints(Player winner, GameEndReason reason) {

        int pointsBonus = 0;

        switch (reason) {
            case GameEndReason.CHECKMATE:
                pointsBonus = 150;
                System.out.println("Victory through Check Mate!");
                break;

            case FORFEIT:
                pointsBonus = 300;
                System.out.println("Victory through the adversary's Forfeit!");
                break;

            case DRAW:
                System.out.println("-------------------------------------");
                System.out.println("--- DRAW ---");
                System.out.println("Points for White Player: " + this.whitePlayer.getPoints());
                System.out.println("Points for Black Player: " + this.blackPlayer.getPoints());
                System.out.println("-------------------------------------");
                return;

            case PAUSE:
            default:
                pointsBonus = 0;
                return;
        }

        winner.setPoints(pointsBonus);

        System.out.println("-------------------------------------");
        System.out.println("--- WINNER: " + winner.getName() + " ---");
        System.out.println("Points from Captures: " + (winner.getPoints() - pointsBonus));
        System.out.println("Points for Victory: " + pointsBonus);
        System.out.println("TOTAL Points from game: " + winner.getPoints());
        System.out.println("-------------------------------------");
    }
}
