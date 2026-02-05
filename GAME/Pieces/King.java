package TEMA1.GAME.Pieces;

import java.util.ArrayList;
import java.util.List;
import TEMA1.GAME.Board;

public class King extends Piece {

    public King(Colors color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> getPossibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();

        Position currentPosition = this.getPosition();
        int y = currentPosition.getY();
        char x = currentPosition.getX();
        Colors pieceColor = this.getColor();

        int[][] directions = {
                {0, 1},   // Up
                {1, 1},   // Up-Right
                {1 ,0},   // Right
                {1, -1},  // Down-Right
                {0, -1},  // Down
                {-1, -1}, // Down-Left
                {-1, 0},  // Left
                {-1, 1}   // Up-Left
        };

        for (int[] direction : directions) {
            int dx = direction[0];
            int dy = direction[1];

            char newX = (char) (x + dx);
            int newY = y + dy;

            // we made it to the end of the board in one direction so we skip to the next direction
            if (!currentPosition.isValidX(newX) || !currentPosition.isValidY(newY)) {
                continue;
            }

            Position newPos = new Position(newX, newY);
            Piece pieceAtNewPos = board.getPieceAt(newPos);

            if (pieceAtNewPos == null) {
                moves.add(newPos);
            } else {
                // we can capture a piece
                if (pieceAtNewPos.getColor() != pieceColor) {
                    moves.add(newPos);
                }
            }
        }

        return moves;
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition) {
        List<Position> moves = getPossibleMoves(board);

        for(Position it : moves) {
            if(it.equals(kingPosition)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public char type() {
        return 'K';
    }

    @Override
    public King deepCopy(Position position) {
        return new King(this.getColor(), position);
    }

    @Override
    public String toString() {
        String res = "type: " + this.type() + ", color: " + this.getColor();
        return res;
    }
}
