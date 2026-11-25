package pos.com.common.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pos.com.common.constant.MessageConstant;
import pos.com.common.dto.PagingRS;
import pos.com.common.dto.StructureRS;

public abstract class BaseController {

    protected static ResponseEntity<StructureRS> response(StructureRS body) {
        return ResponseEntity.status(body.getStatus()).body(body);
    }

    protected static ResponseEntity<StructureRS> response() {
        return response(HttpStatus.OK, MessageConstant.SUCCESS, null);
    }

    protected static <T> ResponseEntity<StructureRS> response(T data) {
        return response(HttpStatus.OK, MessageConstant.SUCCESS, data);
    }

    protected static <T> ResponseEntity<StructureRS> response(T data, PagingRS paging) {
        return ResponseEntity.ok(new StructureRS(HttpStatus.OK, MessageConstant.SUCCESS, data, paging));
    }

    protected static <T> ResponseEntity<StructureRS> created(T data) {
        return response(HttpStatus.CREATED, MessageConstant.CREATED, data);
    }

    protected static ResponseEntity<StructureRS> noContent() {
        return ResponseEntity.noContent().build();
    }

    protected static <T> ResponseEntity<StructureRS> response(HttpStatus status, String message, T data) {
        return ResponseEntity.status(status).body(new StructureRS(status, message, data));
    }
}
