package pos.com.common.exception.httpstatus;

import org.springframework.http.HttpStatus;

public class InternalServerError extends ApiException {
    public InternalServerError(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public InternalServerError(String message, Object data) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message, data);
    }
}
