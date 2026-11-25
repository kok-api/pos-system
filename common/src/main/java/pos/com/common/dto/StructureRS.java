package pos.com.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import pos.com.common.constant.MessageConstant;

import java.time.Instant;
import java.util.Locale;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StructureRS {
    private final int status;
    private final String message;
    private final String messageKey;
    private final Instant timestamp;
    private final Object data;
    private final PagingRS paging;

    public StructureRS() {
        this(HttpStatus.OK, MessageConstant.SUCCESS, null, null);
    }

    public <T> StructureRS(T data) {
        this(HttpStatus.OK, MessageConstant.SUCCESS, data, null);
    }

    public <T> StructureRS(T data, PagingRS paging) {
        this(HttpStatus.OK, MessageConstant.SUCCESS, data, paging);
    }

    public StructureRS(HttpStatusCode status, String message) {
        this(status, message, null, null);
    }

    public <T> StructureRS(HttpStatusCode status, String message, T data) {
        this(status, message, data, null);
    }

    public <T> StructureRS(HttpStatusCode status, String message, T data, PagingRS paging) {
        HttpStatusCode safeStatus = status == null ? HttpStatus.INTERNAL_SERVER_ERROR : status;
        String safeMessage = message == null || message.isBlank()
                ? (safeStatus.value() >= 500
                        ? MessageConstant.INTERNAL_SERVER_ERROR
                        : MessageConstant.BAD_REQUEST)
                : message.trim();
        this.status = safeStatus.value();
        this.message = safeMessage;
        this.messageKey = toMessageKey(safeMessage);
        this.timestamp = Instant.now();
        this.data = data;
        this.paging = paging;
    }

    private static String toMessageKey(String value) {
        String key = value.trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_+|_+$", "");
        return key.isEmpty() ? "message" : key;
    }
}
