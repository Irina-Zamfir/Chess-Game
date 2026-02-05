package TEMA1.GAME.Pieces;

import TEMA1.GAME.Board;

import java.util.List;

public interface ChessPiece {

    /** i note all the posible positions the piece can move to */
    List<Position> getPossibleMoves(Board board);
    boolean checkForCheck(Board board, Position kingPosition);
    char type();

}
