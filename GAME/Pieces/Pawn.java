package TEMA1.GAME.Pieces;

import java.util.ArrayList;
import java.util.List;
import TEMA1.GAME.Board;

public class Pawn extends Piece {

    private boolean firstMove;

    public Pawn(Colors color, Position position) {
        super(color, position);
        this.firstMove = true;
    }

    public boolean isValidY(int y) {
        return y >= 1 && y <= 8;
    }

    public boolean getFirstMove () {
        return this.firstMove;
    }

    public void setFirstMove (boolean state) {
        this.firstMove = state;
    }

    @Override
    public List<Position> getPossibleMoves(Board board) {

        List<Position> moves = new ArrayList<>();

        int direction;
        if(this.getColor() == Colors.WHITE) {
            direction = 1;
        } else {
            direction = -1;
        }

        Position currentPosition = this.getPosition();
        int y = currentPosition.getY();
        char x = currentPosition.getX();
        Colors pieceColor = this.getColor();

        int newY = y + direction;
        if(isValidY(newY)) {
            Position pos1 = new Position(x, newY);

            if(board.getPieceAt(pos1) == null) {
                moves.add(pos1);

                // now I check if I can do the first move
                if(firstMove) {
                    int newY2 = y + 2 * direction;

                    if(isValidY(newY2)) {
                        Position pos2 = new Position(x, newY2);

                        if(board.getPieceAt(pos2) == null) {
                            moves.add(pos2);
                        }
                    }
                }
            }
        }

        // now i verify if i can capture other pieces
        if(x < 'H') {
            char newX = x;
            newX++;
            Position rightDiag = new Position(newX, newY);
            Piece pieceAtNewPos = board.getPieceAt(rightDiag);

            if(pieceAtNewPos != null && pieceAtNewPos.getColor() != pieceColor) {
                moves.add(rightDiag);
            }
        }

        if(x > 'A') {
            char newX = x;
            newX--;
            Position leftDiag = new Position(newX, newY);
            Piece pieceAtNewPos = board.getPieceAt(leftDiag);

            if(pieceAtNewPos != null && pieceAtNewPos.getColor() != pieceColor) {
                moves.add(leftDiag);
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
        return 'P';
    }

    @Override
    public Pawn deepCopy(Position position) {
        return new Pawn(this.getColor(), position);
    }

    @Override
    public String toString() {
        String res = "type: " + this.type() + ", color: " + this.getColor();
        return res;
    }
}
