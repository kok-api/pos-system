package pos.com.common.exception.httpstatus;
import org.springframework.http.HttpStatus;
public class ConflictException extends ApiException {
    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
    public ConflictException(String message, Object data) {
        super(HttpStatus.CONFLICT, message, data);
    }
}
