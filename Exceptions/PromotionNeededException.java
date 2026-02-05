package TEMA1.Exceptions;

import TEMA1.GAME.Pieces.Piece;

public class PromotionNeededException extends RuntimeException {
    private final Piece capturedPiece;

    public PromotionNeededException(String message, Piece capturedPiece) {
        super(message);
        this.capturedPiece = capturedPiece;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }
}
