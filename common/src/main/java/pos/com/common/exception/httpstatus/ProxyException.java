package pos.com.common.exception.httpstatus;

import org.springframework.http.HttpStatus;

public class ProxyException extends ApiException {
    public ProxyException(String message) {
        super(HttpStatus.BAD_GATEWAY, message);
    }

    public ProxyException(String message, Object data) {
        super(HttpStatus.BAD_GATEWAY, message, data);
    }
}
