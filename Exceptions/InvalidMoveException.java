package TEMA1.Exceptions;

//TODO: for Invalid moves (tied to isValidMove -- needed to be added throughout Board and possibly Player)
public class InvalidMoveException extends Exception {
    public InvalidMoveException(String message) {
        super(message);
    }
}
