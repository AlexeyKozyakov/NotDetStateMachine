package koziakov.compillers.notdetstatemachine.exceptions;

public class FileParseException extends Exception{

    public FileParseException(int line, int word) {
        super("Syntax error in line " + line +" word " + word);
    }

}
