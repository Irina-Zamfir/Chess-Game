package TEMA1.GAME.Pieces;

import TEMA1.GAME.Board;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(Colors color, Position position) {
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
                {1, 1},   // Up-Right
                {-1, 1},  // Up-Left
                {-1, -1}, // Down-Left
                {1, -1}   // Down-Right
        };

        for (int[] direction : directions) {
            int dx = direction[0];
            int dy = direction[1];

            for (int k = 1; k < 8; k++) {
                char newX = (char)(x + dx * k);
                int newY = y + dy * k;

                // we made it to the end of the board in one direction so we stop
                if (!currentPosition.isValidX(newX) || !currentPosition.isValidY(newY)) {
                    break;
                }

                Position newPos = new Position(newX, newY);
                Piece pieceAtNewPos = board.getPieceAt(newPos);

                if (pieceAtNewPos == null) {
                    moves.add(newPos);
                }
                else {
                    // we can capture a piece and then stop with this direction
                    if (pieceAtNewPos.getColor() != pieceColor) {
                        moves.add(newPos);
                        break;
                    }
                    else {
                        break;
                    }
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
        return 'B';
    }

    @Override
    public Bishop deepCopy(Position position) {
        return new Bishop(this.getColor(), position);
    }

    @Override
    public String toString() {
        String res = "type: " + this.type() + ", color: " + this.getColor();
        return res;
    }
}
