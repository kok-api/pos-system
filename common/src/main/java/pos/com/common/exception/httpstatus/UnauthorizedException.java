package pos.com.common.exception.httpstatus;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

    public UnauthorizedException(String message, Object data) {
        super(HttpStatus.UNAUTHORIZED, message, data);
    }
}
