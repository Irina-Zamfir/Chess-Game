package TEMA1.GAME.Pieces;

import TEMA1.GAME.Board;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(Colors color, Position position) {
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
                {-1, 2}, {1, 2}, {-2, 1}, {2, 1},
                {-2, -1}, {2, -1}, {-1, -2}, {1, -2}
        };

        for (int[] d : directions) {
            char newX = (char)(x + d[0]);
            int newY = y + d[1];

            if (currentPosition.isValidX(newX) && currentPosition.isValidY(newY)) {
                Position newPos = new Position(newX, newY);
                Piece pieceAtNewPos = board.getPieceAt(newPos);

                if (pieceAtNewPos == null) {
                    moves.add(newPos);
                } else if (pieceAtNewPos.getColor() != pieceColor) {
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
        return 'N';
    }

    @Override
    public Knight deepCopy(Position position) {
        return new Knight(this.getColor(), position);
    }

    @Override
    public String toString() {
        String res = "type: " + this.type() + ", color: " + this.getColor();
        return res;
    }
}
