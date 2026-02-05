package TEMA1.GAME;

import TEMA1.Exceptions.InvalidMoveException;
import TEMA1.Exceptions.PromotionNeededException;
import TEMA1.GAME.Pieces.*;

import java.util.List;
import java.util.TreeSet;

public class Board {

    private TreeSet<ChessPair<Position, Piece>> boardPieces;

    public Board() {
        boardPieces = new TreeSet<>();
    }

    public void initialize() {

        // white pawns
        char x = 'A';
        for (int i = 1; i <= 8; i++) {
            Position pos = new Position(x, 2);
            Pawn pawn = new Pawn(Colors.WHITE, pos);
            ChessPair<Position, Piece> pairPawn = new ChessPair<>(pos, pawn);
            this.boardPieces.add(pairPawn);
            x++;
        }

        // black pawns
        x = 'A';
        for (int i = 1; i <= 8; i++) {
            Position pos = new Position(x, 7);
            Pawn pawn = new Pawn(Colors.BLACK, pos);
            ChessPair<Position, Piece> pairPawn = new ChessPair<>(pos, pawn);
            this.boardPieces.add(pairPawn);
            x++;
        }

        // white rooks
        Position posRookWhite1 = new Position('A', 1);
        Rook rookWhite1 = new Rook(Colors.WHITE, posRookWhite1);
        ChessPair<Position, Piece> pairRookWhite1 = new ChessPair<>(posRookWhite1, rookWhite1);
        this.boardPieces.add(pairRookWhite1);

        Position posRookWhite2 = new Position('H', 1);
        Rook rookWhite2 = new Rook(Colors.WHITE, posRookWhite2);
        ChessPair<Position, Piece> pairRookWhite2 = new ChessPair<>(posRookWhite2, rookWhite2);
        this.boardPieces.add(pairRookWhite2);

        // black rooks
        Position posRookBlack1 = new Position('A', 8);
        Rook rookBlack1 = new Rook(Colors.BLACK, posRookBlack1);
        ChessPair<Position, Piece> pairRookBlack1 = new ChessPair<>(posRookBlack1, rookBlack1);
        this.boardPieces.add(pairRookBlack1);

        Position posRookBlack2 = new Position('H', 8);
        Rook rookBlack2 = new Rook(Colors.BLACK, posRookBlack2);
        ChessPair<Position, Piece> pairRookBlack2 = new ChessPair<>(posRookBlack2, rookBlack2);
        this.boardPieces.add(pairRookBlack2);

        // white knights
        Position posKnightWhite1 = new Position('B', 1);
        Knight knightWhite1 = new Knight(Colors.WHITE, posKnightWhite1);
        ChessPair<Position, Piece> pairKnightWhite1 = new ChessPair<>(posKnightWhite1, knightWhite1);
        this.boardPieces.add(pairKnightWhite1);

        Position posKnightWhite2 = new Position('G', 1);
        Knight knightWhite2 = new Knight(Colors.WHITE, posKnightWhite2);
        ChessPair<Position, Piece> pairKnightWhite2 = new ChessPair<>(posKnightWhite2, knightWhite2);
        this.boardPieces.add(pairKnightWhite2);

        // black knights
        Position posKnightBlack1 = new Position('B', 8);
        Knight knightBlack1 = new Knight(Colors.BLACK, posKnightBlack1);
        ChessPair<Position, Piece> pairKnightBlack1 = new ChessPair<>(posKnightBlack1, knightBlack1);
        this.boardPieces.add(pairKnightBlack1);

        Position posKnightBlack2 = new Position('G', 8);
        Knight knightBlack2 = new Knight(Colors.BLACK, posKnightBlack2);
        ChessPair<Position, Piece> pairKnightBlack2 = new ChessPair<>(posKnightBlack2, knightBlack2);
        this.boardPieces.add(pairKnightBlack2);

        // white bishops
        Position posBishopWhite1 = new Position('C', 1);
        Bishop bishopWhite1 = new Bishop(Colors.WHITE, posBishopWhite1);
        ChessPair<Position, Piece> pairBishopWhite1 = new ChessPair<>(posBishopWhite1, bishopWhite1);
        this.boardPieces.add(pairBishopWhite1);

        Position posBishopWhite2 = new Position('F', 1);
        Bishop bishopWhite2 = new Bishop(Colors.WHITE, posBishopWhite2);
        ChessPair<Position, Piece> pairBishopWhite2 = new ChessPair<>(posBishopWhite2, bishopWhite2);
        this.boardPieces.add(pairBishopWhite2);

        // black bishops
        Position posBishopBlack1 = new Position('C', 8);
        Bishop bishopBlack1 = new Bishop(Colors.BLACK, posBishopBlack1);
        ChessPair<Position, Piece> pairBishopBlack1 = new ChessPair<>(posBishopBlack1, bishopBlack1);
        this.boardPieces.add(pairBishopBlack1);

        Position posBishopBlack2 = new Position('F', 8);
        Bishop bishopBlack2 = new Bishop(Colors.BLACK, posBishopBlack2);
        ChessPair<Position, Piece> pairBishopBlack2 = new ChessPair<>(posBishopBlack2, bishopBlack2);
        this.boardPieces.add(pairBishopBlack2);

        // white queen
        Position posQueenWhite = new Position('D', 1);
        Queen queenWhite = new Queen(Colors.WHITE, posQueenWhite);
        ChessPair<Position, Piece> pairQueenWhite = new ChessPair<>(posQueenWhite, queenWhite);
        this.boardPieces.add(pairQueenWhite);

        // black queen
        Position posQueenBlack = new Position('D', 8);
        Queen queenBlack = new Queen(Colors.BLACK, posQueenBlack);
        ChessPair<Position, Piece> pairQueenBlack = new ChessPair<>(posQueenBlack, queenBlack);
        this.boardPieces.add(pairQueenBlack);

        // white king
        Position posKingWhite = new Position('E', 1);
        King kingWhite = new King(Colors.WHITE, posKingWhite);
        ChessPair<Position, Piece> pairKingWhite = new ChessPair<>(posKingWhite, kingWhite);
        this.boardPieces.add(pairKingWhite);

        // black king
        Position posKingBlack = new Position('E', 8);
        King kingBlack = new King(Colors.BLACK, posKingBlack);
        ChessPair<Position, Piece> pairKingBlack = new ChessPair<>(posKingBlack, kingBlack);
        this.boardPieces.add(pairKingBlack);

    }

    public TreeSet<ChessPair<Position, Piece>> getBoardPieces() {
        return boardPieces;
    }

    public Piece getPieceAt(Position position) {

        for(ChessPair<Position, Piece> it : this.boardPieces) {
            if(it.getKey().equals(position)) {
                return it.getValue();
            }
        }

        return null;
    }

    public void initializeBoardFromList(List<Piece> newBoardPiece) {
        boardPieces.clear();

        for (Piece piece : newBoardPiece) {
            Position pos = piece.getPosition();
            ChessPair<Position, Piece> pair = new ChessPair<>(pos, piece);
            boardPieces.add(pair);
        }
    }

    // find the pair in Board, using Position
    public ChessPair<Position, Piece> getPairAt(Position position) {
        ChessPair<Position, Piece> pair = new ChessPair<>(position, null);

        for (ChessPair<Position, Piece> it : this.boardPieces) {
            if (it.getKey().equals(position)) {
                return it;
            }
        }

        return null;
    }

    public Piece movePiece(Position from, Position to, Colors currentPlayerColor)
            throws InvalidMoveException, PromotionNeededException {

        // I need to move the first part in the other function cuz we also need to return the captured piece
        // checks if the move is valid
        Piece pieceAtFrom = getPieceAt(from);
        if (pieceAtFrom == null) {
            throw new InvalidMoveException("There's no piece at this position");
        }

        if (pieceAtFrom.getColor() != currentPlayerColor) {
            throw new InvalidMoveException("The piece at this position is not a piece of your color");
        }

        if (!isValidMove(from,  to)) {
            throw new InvalidMoveException("Invalid Move");
        }

        ChessPair<Position, Piece> pairToMove = this.getPairAt(from);
        Piece pieceToMove = pairToMove.getValue();

        ChessPair<Position, Piece> pairToCapture = this.getPairAt(to);
        Piece capturedPiece = null;

        if (pairToCapture != null) {
            capturedPiece = pairToCapture.getValue();
            this.boardPieces.remove(pairToCapture);
        }

        this.boardPieces.remove(pairToMove);

        // sets the new position
        pieceToMove.setPosition(to);

        // create and add new pair to the tree set
        ChessPair<Position, Piece> newPair = new ChessPair<>(pieceToMove.getPosition(), pieceToMove);
        this.boardPieces.add(newPair);

        // Checking Pawn Possibilities
        if (pieceToMove.type() == 'P') {

            Pawn pawn = (Pawn)pieceToMove;

            // checking if this is the pawns first move
            if (pawn.getFirstMove()) {
                pawn.setFirstMove(false);
            }

            // checking if it made to the last row
            if ((pawn.getPosition().getY() == 8 && pawn.getColor() == Colors.WHITE) ||
                    (pawn.getPosition().getY() == 1 && pawn.getColor() == Colors.BLACK)) {
                throw new PromotionNeededException("Promotion Needed", capturedPiece);
            }
        }

        return capturedPiece;
    }

    public void applyPromotion(Position promotionPosition, Colors color, char chosenType) {

        Position newPos = promotionPosition;
        ChessPair<Position, Piece> pawnPair = this.getPairAt(promotionPosition);
        if (pawnPair != null) {
            this.boardPieces.remove(pawnPair);
        }

        Piece newPiece = switch (chosenType) {
            case 'Q' -> new Queen(color, newPos);
            case 'R' -> new Rook(color, newPos);
            case 'B' -> new Bishop(color, newPos);
            case 'N' -> new Knight(color, newPos);
            default ->
                    throw new IllegalArgumentException("Tip de piesă de promovare invalid.");
        };

        ChessPair<Position, Piece> newPair = new ChessPair<>(newPos, newPiece);
        this.boardPieces.add(newPair);
    }

    // finds the position of the king of the same color as the piece that we want to move
    public Position findKing(Colors color) {
        for (ChessPair<Position, Piece> it : this.boardPieces) {
            Piece piece = it.getValue();
            if (piece.type() == 'K' && piece.getColor() == color) {
                return it.getKey();
            }
        }
        return null;
    }

    // gets a makeshift boardPieces and checks all the other pieces that are not the kings color
    public boolean isKingInCheck(Colors color) {

        Position kingPosition = findKing(color);
        for (ChessPair<Position, Piece> it : this.boardPieces) {
            Piece piece = it.getValue();
            if (piece.getColor() != color) {
               if(piece.checkForCheck(this, kingPosition)) {
                   return true;
                }
            }
        }

        return false;
    }

    // method that makes a deep copy of the boardPieces in order to verify the validity of a move
    public Board deepCopy() {
        Board copy = new Board();

        for (ChessPair<Position, Piece> pair : this.boardPieces) {
            Position newPosition = pair.getKey().deepCopy();

            Piece originalPiece = pair.getValue();
            Piece newPiece = originalPiece.deepCopy(newPosition);

            copy.boardPieces.add(new ChessPair<>(newPosition, newPiece));
        }

        return copy;
    }

    public boolean isValidMove(Position from, Position to) {

        // check if there is a piece at the from position
        Piece pieceToMove = getPieceAt(from);
        if (pieceToMove == null) {
            return false;
        }

        // checks if the to position is in the list of moves
        List<Position> possibleMoves = pieceToMove.getPossibleMoves(this);
        if (!possibleMoves.contains(to)) {
            return false;
        }

        // creates a simulation to check if the king is in check
        Board copy = deepCopy();
        ChessPair<Position, Piece> pairAtFromPos = copy.getPairAt(from);
        Piece pieceAtFromPos = pairAtFromPos.getValue();

        // to check if there is a pair to be captured at the to position
        ChessPair<Position, Piece> pairToCapture = copy.getPairAt(to);

        if (pairToCapture != null) {
            copy.boardPieces.remove(pairToCapture);
        }

        // removes pair from the old position (from)
        copy.boardPieces.remove(pairAtFromPos);

        // sets the new position
        pieceAtFromPos.setPosition(to);

        // creates a new pair to add into the boardPieces
        ChessPair<Position, Piece> newPair = new ChessPair<>(pieceAtFromPos.getPosition(), pieceAtFromPos);
        copy.boardPieces.add(newPair);

        // check if king is in check
        if (copy.isKingInCheck(pieceAtFromPos.getColor())) {
            return false;
        }

        return true;
    }
}
