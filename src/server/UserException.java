package server;

public class UserException extends Exception{
    @Override
    public String toString() {
        return "User already in ArrayList loggedInUsers";
    }
}
