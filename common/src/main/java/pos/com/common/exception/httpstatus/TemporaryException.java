package pos.com.common.exception.httpstatus;

import org.springframework.http.HttpStatus;

public class TemporaryException extends ApiException {
    public TemporaryException(String message) {
        super(HttpStatus.SERVICE_UNAVAILABLE, message);
    }

    public TemporaryException(String message, Object data) {
        super(HttpStatus.SERVICE_UNAVAILABLE, message, data);
    }
}
