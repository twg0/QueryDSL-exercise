package hello.test.querydsl.domain.exception;

public class NotEnoughStockException extends RuntimeException{

    public NotEnoughStockException() {};

    public NotEnoughStockException(String message) {
        super(message);
    }

    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }
}
