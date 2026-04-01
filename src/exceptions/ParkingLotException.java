package exceptions;

// Custom exception for parking errors
public class ParkingLotException extends RuntimeException {
    public ParkingLotException(String message) {
        super(message);
    }
}