package TEMA1.GAME.Pieces;

public abstract class Piece implements ChessPiece {

    private Colors color;
    private Position position;

    public Piece(Colors color, Position position) {
        this.color = color;
        this.position = position;
    }

    public Colors getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position newPosition) {
        this.position = newPosition;
    }

    public abstract Piece deepCopy(Position position);

    public abstract String toString();

    // mai punem functii i guess

}
