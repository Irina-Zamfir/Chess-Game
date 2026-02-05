package TEMA1.GAME;

import TEMA1.Exceptions.InvalidMoveException;
import TEMA1.Exceptions.PromotionNeededException;
import TEMA1.GAME.Pieces.Colors;
import TEMA1.GAME.Pieces.Piece;
import TEMA1.GAME.Pieces.Position;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private Colors color;
    private List<Piece> capturedPieces;
    private int points;

    public Player(String name, Colors color) {
        this.name = name;
        this.color = color;
        this.capturedPieces = new ArrayList<>();
        this.points = 0;
    }

    public String getName() {
        return name;
    }

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors newColor) {
        this.color = newColor;
    }

    public List<Piece> getCapturedPieces() {
        return capturedPieces;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int newPoints) {
        this.points += newPoints;
    }

    // returns the current list of pieces of the player that are at one moment on the board
    public List<ChessPair<Position, Piece>> getOwnedPieces(Board board) {
        List<ChessPair<Position, Piece>> playerPieces = new ArrayList<>();

        for (ChessPair<Position, Piece> piece : board.getBoardPieces()) {
            if (piece.getValue().getColor() == this.color) {
                playerPieces.add(piece);
            }
        }

        return playerPieces;
    }

    public int calculatePoints(Piece piece) {

        int newPoints = 0;
        if (piece.type() == 'Q') {
            newPoints += 90;
        } else if (piece.type() == 'R') {
            newPoints += 50;
        } else if (piece.type() == 'B' || piece.type() == 'N') {
            newPoints += 30;
        } else if (piece.type() == 'P') {
            newPoints += 10;
        }

        return newPoints;
    }

    public Piece makeMove(Position from, Position to, Board board) throws InvalidMoveException {

        Piece capturedPiece = null;

        try {
            capturedPiece = board.movePiece(from, to, color);

            if (capturedPiece != null) {
                capturedPieces.add(capturedPiece);
                setPoints(calculatePoints(capturedPiece));
            }

        } catch (PromotionNeededException e) {
            capturedPiece = e.getCapturedPiece();
            if (capturedPiece != null) {
                capturedPieces.add(capturedPiece);
                setPoints(calculatePoints(capturedPiece));
            }

            throw e;
        }

        return capturedPiece;
    }
}
