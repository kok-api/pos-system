package pos.com.common.exception.httpstatus;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends ApiException {
    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }

    public ForbiddenException(String message, Object data) {
        super(HttpStatus.FORBIDDEN, message, data);
    }
}
