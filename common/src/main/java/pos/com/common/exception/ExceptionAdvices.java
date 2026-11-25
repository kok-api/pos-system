package pos.com.common.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import pos.com.common.constant.MessageConstant;
import pos.com.common.dto.StructureRS;
import pos.com.common.exception.httpstatus.ApiException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvices {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<StructureRS> handleApiException(ApiException ex) {
        log.warn("API exception [{}]: {}", ex.getStatus().value(), ex.getMessage());

        return build(ex.getStatus(), ex.getMessage(), ex.getData());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StructureRS> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());

        return build(HttpStatus.FORBIDDEN, MessageConstant.FORBIDDEN, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StructureRS> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return buildValidationResponse(ex);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<StructureRS> handleBindException(BindException ex) {
        return buildValidationResponse(ex);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<StructureRS> handleMethodValidation(HandlerMethodValidationException ex) {
        Map<String, String> errors = new LinkedHashMap<>();

        for (ParameterValidationResult result : ex.getParameterValidationResults()) {
            String name = result.getMethodParameter().getParameterName();
            if (name == null || name.isBlank()) {
                name = "arg" + result.getMethodParameter().getParameterIndex();
            }
            String message = result.getResolvableErrors().stream()
                    .map(MessageSourceResolvable::getDefaultMessage)
                    .filter(value -> value != null && !value.isBlank())
                    .collect(Collectors.joining("; "));
            errors.putIfAbsent(name, message.isEmpty() ? "Invalid value" : message);
        }

        return build(HttpStatus.BAD_REQUEST, MessageConstant.VALIDATION_FAILED, errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<StructureRS> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new LinkedHashMap<>();

        ex.getConstraintViolations().forEach(violation -> errors.put(violation.getPropertyPath().toString(), violation.getMessage()));

        return build(HttpStatus.BAD_REQUEST, MessageConstant.VALIDATION_FAILED, errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StructureRS> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String requiredType = ex.getRequiredType() == null ? "valid type" : ex.getRequiredType().getSimpleName();

        String message = String.format("Parameter '%s' must be of type %s", ex.getName(), requiredType);

        return build(HttpStatus.BAD_REQUEST, message, null);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<StructureRS> handleMissingParameter(MissingServletRequestParameterException ex) {
        String message = String.format("Required parameter '%s' is missing", ex.getParameterName());

        return build(HttpStatus.BAD_REQUEST, message, null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StructureRS> handleUnreadableMessage(HttpMessageNotReadableException ex) {
        log.debug("Unreadable request body", ex);

        return build(HttpStatus.BAD_REQUEST, "Request body is missing or malformed", null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<StructureRS> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String supportedMethods = ex.getSupportedHttpMethods() == null || ex.getSupportedHttpMethods().isEmpty() ? "none" : ex.getSupportedHttpMethods().stream().map(Object::toString).collect(Collectors.joining(", "));

        String message = String.format("HTTP method '%s' is not supported. Supported methods: %s", ex.getMethod(), supportedMethods);

        return build(HttpStatus.METHOD_NOT_ALLOWED, message, null);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<StructureRS> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        String supportedMediaTypes = ex.getSupportedMediaTypes().isEmpty() ? "none" : ex.getSupportedMediaTypes().stream().map(Object::toString).collect(Collectors.joining(", "));

        String message = String.format("Media type '%s' is not supported. Supported media types: %s", ex.getContentType(), supportedMediaTypes);

        return build(HttpStatus.UNSUPPORTED_MEDIA_TYPE, message, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StructureRS> handleUnexpectedException(Exception ex) {
        log.error("Unhandled application exception", ex);

        return build(HttpStatus.INTERNAL_SERVER_ERROR, MessageConstant.INTERNAL_SERVER_ERROR, null);
    }

    private ResponseEntity<StructureRS> buildValidationResponse(BindException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, error -> error.getDefaultMessage() == null ? "Invalid value" : error.getDefaultMessage(), (first, ignored) -> first, LinkedHashMap::new));

        ex.getBindingResult().getGlobalErrors().forEach(error -> errors.putIfAbsent(error.getObjectName(), error.getDefaultMessage() == null ? "Invalid value" : error.getDefaultMessage()));

        return build(HttpStatus.BAD_REQUEST, MessageConstant.VALIDATION_FAILED, errors);
    }

    private ResponseEntity<StructureRS> build(HttpStatus status, String message, Object data) {
        return ResponseEntity.status(status).body(new StructureRS(status, message, data));
    }
}
