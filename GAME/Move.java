package TEMA1.GAME;

import TEMA1.GAME.Pieces.Colors;
import TEMA1.GAME.Pieces.Piece;
import TEMA1.GAME.Pieces.Position;

public class Move {

    private Colors color;
    private Position from;
    private Position to;
    private Piece capturedPiece;
    private char movingPieceType;

    public Move(Colors color, Position from, Position to, Piece piece, char type) {
        this.color = color;
        this.from = from;
        this.to = to;
        this.capturedPiece = piece;
        this.movingPieceType = type;
    }

    public Move getMove() {
        return this;
    }

    public Piece getCapturedPiece() {
        return this.capturedPiece;
    }

    public Colors getPlayerColor() {
        return this.color;
    }

    public Position getFrom() {
        return this.from;
    }

    public Position getTo() {
        return this.to;
    }

    @Override
    public String toString() {
        String res = this.color + ": " + this.capturedPiece + " " + this.from + "-" + this.to;
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;

        // Verificam daca toate componentele mutarii sunt identice
        return movingPieceType == move.movingPieceType &&
                color == move.color &&
                from.equals(move.from) &&
                to.equals(move.to) &&
                (capturedPiece == null ? move.capturedPiece == null :
                        move.capturedPiece != null && capturedPiece.type() == move.capturedPiece.type());
    }
}
