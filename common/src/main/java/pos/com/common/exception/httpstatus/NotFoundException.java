package pos.com.common.exception.httpstatus;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public NotFoundException(String message, Object data) {
        super(HttpStatus.NOT_FOUND, message, data);
    }
}
