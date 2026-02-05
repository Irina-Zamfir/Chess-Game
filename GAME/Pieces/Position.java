package TEMA1.GAME.Pieces;

public class Position implements Comparable<Position>{
    private char x; /* from A to H */
    private int y; /* from 1 to 8 */
    // private boolean occupied;  // -- ca sa vedem daca e o piesa pe pozitie sau nu

    public Position(char x, int y) {
        this.x = x;
        this.y = y;
    }

    public char getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int newY) {
        y = newY;
    }

    public boolean isValidY(int y) {
        return y >= 1 && y <= 8;
    }

    public boolean isValidX(char x) {
        return x >= 'A' && x <= 'H';
    }

    public Position deepCopy() {
        return new Position(this.getX(), this.getY());
    }

    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }

        if(!(obj instanceof Position)) {
            return false;
        }

        Position other = (Position) obj;
        return this.x == other.x && this.y == other.y;
    }

    public String toString() {
        String res = "" + getX() + getY();
        return res;
    }

    public int compareTo(Position position) {
        if(this == position)
            return 0;

        if(this.getY() < position.getY()) {
            return -1;
        } else if(this.getY() > position.getY() ) {
            return 1;
        }
        else {
            if(this.getX() < position.getX()) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
