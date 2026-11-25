package pos.com.common.service;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import pos.com.common.constant.MessageConstant;
import pos.com.common.dto.PagingRS;
import pos.com.common.dto.StructureRS;

public abstract class BaseService {
    protected static <T> StructureRS response(T data) {
        return new StructureRS(HttpStatus.OK, MessageConstant.SUCCESS, data);
    }

    protected static <T> StructureRS response(T data, PagingRS paging) {
        return new StructureRS(HttpStatus.OK, MessageConstant.SUCCESS, data, paging);
    }

    protected static <T, S> StructureRS response(T data, Page<S> page) {
        return response(data, new PagingRS(page));
    }

    protected static StructureRS response(HttpStatus status, String message) {
        return new StructureRS(status, message);
    }

    protected static <T> StructureRS response(HttpStatus status, String message, T data) {
        return new StructureRS(status, message, data);
    }
}
