package pos.com.common;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.method.MethodValidationResult;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import pos.com.common.config.CommonAutoConfiguration;
import pos.com.common.constant.MessageConstant;
import pos.com.common.dto.StructureRS;
import pos.com.common.exception.ExceptionAdvices;
import pos.com.common.exception.httpstatus.ApiException;
import pos.com.common.exception.httpstatus.BadRequestException;
import pos.com.common.exception.httpstatus.ConflictException;
import pos.com.common.exception.httpstatus.ForbiddenException;
import pos.com.common.exception.httpstatus.GoneException;
import pos.com.common.exception.httpstatus.InternalServerError;
import pos.com.common.exception.httpstatus.NotFoundException;
import pos.com.common.exception.httpstatus.ProxyException;
import pos.com.common.exception.httpstatus.TemporaryException;
import pos.com.common.exception.httpstatus.UnauthorizedException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExceptionFunctionsTest {

    static final class Payload {
        @NotBlank(message = "must not be blank")
        private String name;
    }

    private static void target(String value) {
    }

    private final ExceptionAdvices advices = new ExceptionAdvices();

    @Test
    void apiExceptionsCarryTheExpectedHttpStatusAndData() {
        assertStatus(new BadRequestException("bad"), 400);
        assertStatus(new UnauthorizedException("unauthorized"), 401);
        assertStatus(new ForbiddenException("forbidden"), 403);
        assertStatus(new NotFoundException("missing"), 404);
        assertStatus(new ConflictException("conflict"), 409);
        assertStatus(new GoneException("gone"), 410);
        assertStatus(new InternalServerError("boom"), 500);
        assertStatus(new ProxyException("upstream"), 502);
        assertStatus(new TemporaryException("later"), 503);

        ApiException withData = new BadRequestException("bad", "details");
        assertEquals("bad", withData.getMessage());
        assertEquals("details", withData.getData());
    }

    @Test
    void handleApiExceptionUsesStatusMessageAndData() {
        ResponseEntity<StructureRS> response =
                advices.handleApiException(new NotFoundException("nope", "detail"));

        assertEquals(404, response.getStatusCode().value());
        assertEquals("nope", response.getBody().getMessage());
        assertEquals("detail", response.getBody().getData());
    }

    @Test
    void handleAccessDeniedReturnsForbidden() {
        ResponseEntity<StructureRS> response =
                advices.handleAccessDenied(new AccessDeniedException("denied"));

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode().value());
        assertEquals(MessageConstant.FORBIDDEN, response.getBody().getMessage());
    }

    @Test
    void handleMethodArgumentNotValidCollectsFieldErrors() throws Exception {
        MethodParameter parameter =
                new MethodParameter(ExceptionFunctionsTest.class.getDeclaredMethod("target", String.class), 0);
        BeanPropertyBindingResult binding = new BeanPropertyBindingResult(new Payload(), "payload");
        binding.addError(new FieldError("payload", "name", "must not be blank"));

        ResponseEntity<StructureRS> response =
                advices.handleMethodArgumentNotValid(new MethodArgumentNotValidException(parameter, binding));

        assertEquals(400, response.getStatusCode().value());
        assertEquals(MessageConstant.VALIDATION_FAILED, response.getBody().getMessage());
        assertTrue(errors(response).containsKey("name"));
    }

    @Test
    void handleBindExceptionCollectsFieldAndGlobalErrors() {
        BindException exception = new BindException(new Payload(), "payload");
        exception.addError(new FieldError("payload", "name", "must not be blank"));
        exception.addError(new ObjectError("payload", "global problem"));

        ResponseEntity<StructureRS> response = advices.handleBindException(exception);

        assertEquals(400, response.getStatusCode().value());
        Map<String, String> errors = errors(response);
        assertEquals("must not be blank", errors.get("name"));
        assertEquals("global problem", errors.get("payload"));
    }

    @Test
    void handleMethodValidationCollectsParameterErrors() throws Exception {
        MethodParameter parameter =
                new MethodParameter(ExceptionFunctionsTest.class.getDeclaredMethod("target", String.class), 0);
        MessageSourceResolvable error = mock(MessageSourceResolvable.class);
        when(error.getDefaultMessage()).thenReturn("must not be blank");
        ParameterValidationResult result = mock(ParameterValidationResult.class);
        when(result.getMethodParameter()).thenReturn(parameter);
        when(result.getResolvableErrors()).thenReturn(List.of(error));
        MethodValidationResult validationResult = mock(MethodValidationResult.class);
        when(validationResult.getParameterValidationResults()).thenReturn(List.of(result));

        ResponseEntity<StructureRS> response = advices.handleMethodValidation(
                new HandlerMethodValidationException(validationResult));

        assertEquals(400, response.getStatusCode().value());
        Map<String, String> errors = errors(response);
        assertEquals(1, errors.size());
        assertEquals("must not be blank", errors.values().iterator().next());
    }

    @Test
    void autoConfigurationRegistersExceptionAdvicesForConsumers() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(CommonAutoConfiguration.class))
                .run(context -> assertThat(context).hasSingleBean(ExceptionAdvices.class));
    }

    @Test
    void handleConstraintViolationCollectsViolations() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        ConstraintViolationException exception = new ConstraintViolationException(
                validator.validate(new Payload()));

        ResponseEntity<StructureRS> response = advices.handleConstraintViolation(exception);

        assertEquals(400, response.getStatusCode().value());
        assertTrue(errors(response).containsKey("name"));
    }

    @Test
    void handleTypeMismatchNamesTheInvalidParameter() {
        ResponseEntity<StructureRS> response = advices.handleTypeMismatch(
                new MethodArgumentTypeMismatchException("abc", Integer.class, "id", null, null));

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Parameter 'id' must be of type Integer", response.getBody().getMessage());
    }

    @Test
    void handleMissingParameterNamesTheMissingParameter() {
        ResponseEntity<StructureRS> response = advices.handleMissingParameter(
                new MissingServletRequestParameterException("id", "Long"));

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Required parameter 'id' is missing", response.getBody().getMessage());
    }

    @Test
    void handleUnreadableMessageReturnsBadRequest() {
        ResponseEntity<StructureRS> response = advices.handleUnreadableMessage(
                new HttpMessageNotReadableException("bad body", null));

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Request body is missing or malformed", response.getBody().getMessage());
    }

    @Test
    void handleMethodNotSupportedListsSupportedMethods() {
        ResponseEntity<StructureRS> response = advices.handleMethodNotSupported(
                new HttpRequestMethodNotSupportedException("PATCH", List.of("GET", "POST")));

        assertEquals(405, response.getStatusCode().value());
        assertTrue(response.getBody().getMessage().contains("PATCH"));
        assertTrue(response.getBody().getMessage().contains("GET"));
    }

    @Test
    void handleMediaTypeNotSupportedListsSupportedMediaTypes() {
        ResponseEntity<StructureRS> response = advices.handleMediaTypeNotSupported(
                new HttpMediaTypeNotSupportedException(
                        MediaType.APPLICATION_JSON, List.of(MediaType.TEXT_PLAIN)));

        assertEquals(415, response.getStatusCode().value());
        assertTrue(response.getBody().getMessage().contains("application/json"));
        assertTrue(response.getBody().getMessage().contains("text/plain"));
    }

    @Test
    void handleUnexpectedExceptionHidesInternalDetails() {
        ResponseEntity<StructureRS> response =
                advices.handleUnexpectedException(new IllegalStateException("secret"));

        assertEquals(500, response.getStatusCode().value());
        assertEquals(MessageConstant.INTERNAL_SERVER_ERROR, response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    private void assertStatus(ApiException exception, int expectedStatus) {
        assertEquals(expectedStatus, exception.getStatus().value());
        assertNotNull(exception.getMessage());
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> errors(ResponseEntity<StructureRS> response) {
        return (Map<String, String>) response.getBody().getData();
    }
}
