package pos.com.common.exception.httpstatus;

import org.springframework.http.HttpStatus;

public class GoneException extends ApiException {
    public GoneException(String message) {
        super(HttpStatus.GONE, message);
    }

    public GoneException(String message, Object data) {
        super(HttpStatus.GONE, message, data);
    }
}
