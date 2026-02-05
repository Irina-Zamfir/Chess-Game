package TEMA1.GAME.Pieces;

import java.util.ArrayList;
import java.util.List;
import TEMA1.GAME.Board;

public class Rook extends Piece {
    public Rook(Colors color, Position position) {
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
                {1 ,0},   // Right
                {-1, 0},  // Left
                {0, -1}   // Down
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
        return 'R';
    }

    @Override
    public Rook deepCopy(Position position) {
        return new Rook(this.getColor(), position);
    }

    @Override
    public String toString() {
        String res = "type: " + this.type() + ", color: " + this.getColor();
        return res;
    }
}
