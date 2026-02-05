package TEMA1.Exceptions;

// for invalid commands : if the user types in something wrong , this exception is activated
// todo: check and figure out in which functions i need to be using this
public class InvalidCommandException extends RuntimeException {
    public InvalidCommandException(String message) {
        super(message);
    }
}
