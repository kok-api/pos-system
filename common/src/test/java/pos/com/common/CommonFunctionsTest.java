package pos.com.common;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pos.com.common.constant.MessageConstant;
import pos.com.common.controller.BaseController;
import pos.com.common.dto.BaseListingRQ;
import pos.com.common.dto.PagingRS;
import pos.com.common.dto.StructureRS;
import pos.com.common.entity.BaseEntity;
import pos.com.common.enums.CurrencyType;
import pos.com.common.enums.OrderStatus;
import pos.com.common.enums.PaymentMethod;
import pos.com.common.enums.PaymentStatus;
import pos.com.common.enums.PromotionType;
import pos.com.common.enums.PurchaseStatus;
import pos.com.common.enums.RamSize;
import pos.com.common.enums.StorageSize;
import pos.com.common.enums.UnitType;
import pos.com.common.exception.httpstatus.BadRequestException;
import pos.com.common.pagination.PageRequestFactory;
import pos.com.common.service.BaseService;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonFunctionsTest {

    static final class TestController extends BaseController {
        static ResponseEntity<StructureRS> raw(StructureRS body) {
            return response(body);
        }

        static ResponseEntity<StructureRS> ok() {
            return response();
        }

        static ResponseEntity<StructureRS> ok(Object data) {
            return response(data);
        }

        static ResponseEntity<StructureRS> paged(Object data, PagingRS paging) {
            return response(data, paging);
        }

        static ResponseEntity<StructureRS> createResponse(Object data) {
            return created(data);
        }

        static ResponseEntity<StructureRS> emptyResponse() {
            return noContent();
        }

        static ResponseEntity<StructureRS> custom(HttpStatus status, String message, Object data) {
            return response(status, message, data);
        }
    }

    static final class TestService extends BaseService {
        static StructureRS data(Object data) {
            return response(data);
        }

        static StructureRS paged(Object data, PagingRS paging) {
            return response(data, paging);
        }

        static StructureRS paged(Object data, Page<?> page) {
            return response(data, page);
        }

        static StructureRS status(HttpStatus status, String message) {
            return response(status, message);
        }

        static StructureRS status(HttpStatus status, String message, Object data) {
            return response(status, message, data);
        }
    }

    static final class TestEntity extends BaseEntity {
    }

    @Test
    void structureRsConstructorsCoverEveryResponseShape() {
        StructureRS empty = new StructureRS();
        assertEquals(200, empty.getStatus());
        assertEquals(MessageConstant.SUCCESS, empty.getMessage());
        assertEquals("request_completed_successfully", empty.getMessageKey());
        assertNotNull(empty.getTimestamp());
        assertNull(empty.getData());
        assertNull(empty.getPaging());

        StructureRS withData = new StructureRS("payload");
        assertEquals("payload", withData.getData());
        assertNull(withData.getPaging());

        PagingRS paging = new PagingRS(1, 20, 3, 50);
        StructureRS withPaging = new StructureRS("payload", paging);
        assertEquals("payload", withPaging.getData());
        assertEquals(paging, withPaging.getPaging());

        StructureRS statusOnly = new StructureRS(HttpStatus.CREATED, MessageConstant.CREATED);
        assertEquals(201, statusOnly.getStatus());
        assertEquals(MessageConstant.CREATED, statusOnly.getMessage());

        StructureRS statusWithData = new StructureRS(HttpStatus.NOT_FOUND, MessageConstant.NOT_FOUND, "missing");
        assertEquals(404, statusWithData.getStatus());
        assertEquals("missing", statusWithData.getData());

        StructureRS full = new StructureRS(HttpStatus.OK, MessageConstant.SUCCESS, "data", paging);
        assertEquals("data", full.getData());
        assertEquals(paging, full.getPaging());
    }

    @Test
    void structureRsFallsBackToInternalServerErrorForNullInputs() {
        StructureRS rs = new StructureRS(null, "  ", null, null);

        assertEquals(500, rs.getStatus());
        assertEquals(MessageConstant.INTERNAL_SERVER_ERROR, rs.getMessage());
        assertEquals("an_unexpected_error_occurred", rs.getMessageKey());
    }

    @Test
    void structureRsUsesStatusAppropriateFallbackMessage() {
        StructureRS badRequest = new StructureRS(HttpStatus.BAD_REQUEST, "  ", null, null);
        assertEquals(400, badRequest.getStatus());
        assertEquals(MessageConstant.BAD_REQUEST, badRequest.getMessage());

        StructureRS punctuation = new StructureRS(HttpStatus.OK, "!!!", null, null);
        assertEquals("message", punctuation.getMessageKey());
    }

    @Test
    void structureRsOmitsNullFieldsFromJson() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new StructureRS("payload"));

        assertTrue(json.contains("\"status\":200"));
        assertTrue(json.contains("\"data\":\"payload\""));
        assertFalse(json.contains("paging"));
    }

    @Test
    void pagingRsBuildsFromValuesAndFromPage() {
        PagingRS direct = new PagingRS(2, 10, 5, 100);
        assertEquals(2, direct.getPage());
        assertEquals(10, direct.getSize());
        assertEquals(5, direct.getTotalPages());
        assertEquals(100, direct.getTotalElements());

        Page<String> page = new PageImpl<>(List.of("a"), PageRequest.of(1, 10), 42);
        PagingRS fromPage = new PagingRS(page);
        assertEquals(1, fromPage.getPage());
        assertEquals(10, fromPage.getSize());
        assertEquals(5, fromPage.getTotalPages());
        assertEquals(42, fromPage.getTotalElements());

        PagingRS clamped = new PagingRS(-3, -3, -3, -3);
        assertEquals(0, clamped.getPage());
        assertEquals(0, clamped.getSize());
        assertEquals(0, clamped.getTotalPages());
        assertEquals(0, clamped.getTotalElements());
    }

    @Test
    void baseListingRqNormalizesDefaultsAndExplicitValues() {
        BaseListingRQ rq = new BaseListingRQ();
        assertEquals(0, rq.getPage());
        assertEquals(20, rq.getSize());
        assertEquals("ALL", rq.getQuery());
        assertFalse(rq.hasQuery());
        assertEquals("id", rq.getSort());
        assertEquals("DESC", rq.getOrder());

        rq.setPage(3);
        rq.setSize(500);
        rq.setQuery("  phone  ");
        rq.setSort("  name  ");
        rq.setOrder("asc");

        assertEquals(2, rq.getPage());
        assertEquals(100, rq.getSize());
        assertEquals("phone", rq.getQuery());
        assertTrue(rq.hasQuery());
        assertEquals("name", rq.getSort());
        assertEquals("ASC", rq.getOrder());

        rq.setQuery(" all ");
        assertFalse(rq.hasQuery());

        rq.setPage(0);
        rq.setSize(0);
        assertEquals(0, rq.getPage());
        assertEquals(1, rq.getSize());
    }

    @Test
    void baseListingRqCreatesSafePageables() {
        BaseListingRQ rq = new BaseListingRQ();

        Pageable defaultPageable = rq.getPageable();
        assertEquals(0, defaultPageable.getPageNumber());
        assertEquals(20, defaultPageable.getPageSize());
        assertEquals(Sort.Direction.DESC, defaultPageable.getSort().getOrderFor("id").getDirection());

        Pageable byColumn = rq.getPageable("name");
        assertEquals("name", byColumn.getSort().getOrderFor("name").getProperty());

        Pageable ascending = rq.getPageable((String) null, "asc");
        assertEquals(Sort.Direction.ASC, ascending.getSort().getOrderFor("id").getDirection());

        Pageable invalidDirection = rq.getPageable("name", "sideways");
        assertEquals(Sort.Direction.DESC, invalidDirection.getSort().getOrderFor("name").getDirection());
    }

    @Test
    void baseListingRqCanWhitelistSortFields() {
        BaseListingRQ rq = new BaseListingRQ();
        rq.setPage(2);

        Pageable safe = rq.getPageable(Set.of("id", "name"), "id");
        assertEquals(1, safe.getPageNumber());
        assertEquals("id", safe.getSort().getOrderFor("id").getProperty());

        rq.setSort("secret");
        assertThrows(BadRequestException.class,
                () -> rq.getPageable(Set.of("id", "name"), "id"));
    }

    @Test
    void pageRequestFactoryCreatesValidPageablesAndNormalizesText() {
        Set<String> allowed = Set.of("id", "name");

        Pageable explicit = PageRequestFactory.create(1, 10, " name ", "asc", allowed, "id");
        assertEquals(0, explicit.getPageNumber());
        assertEquals(10, explicit.getPageSize());
        assertEquals(Sort.Direction.ASC, explicit.getSort().getOrderFor("name").getDirection());

        Pageable defaulted = PageRequestFactory.create(0, 10, null, null, allowed, "id");
        assertEquals("id", defaulted.getSort().getOrderFor("id").getProperty());
        assertEquals(Sort.Direction.DESC, defaulted.getSort().getOrderFor("id").getDirection());

        assertNull(PageRequestFactory.normalize(null));
        assertNull(PageRequestFactory.normalize("  "));
        assertEquals("name", PageRequestFactory.normalize("  name  "));
    }

    @Test
    void pageRequestFactoryRejectsInvalidInputWithBadRequest() {
        Set<String> allowed = Set.of("id", "name");

        assertThrows(BadRequestException.class,
                () -> PageRequestFactory.create(1, 10, "secret", "ASC", allowed, "id"));
        assertThrows(BadRequestException.class,
                () -> PageRequestFactory.create(1, 10, "id", "sideways", allowed, "id"));
        assertThrows(BadRequestException.class,
                () -> PageRequestFactory.create(1, 0, "id", "ASC", allowed, "id"));
        assertThrows(BadRequestException.class,
                () -> PageRequestFactory.create(1, 10, null, "ASC", null, "id"));
        assertThrows(BadRequestException.class,
                () -> PageRequestFactory.create(1, 10, null, "ASC", allowed, null));
    }

    @Test
    void baseServiceResponseHelpersCoverEveryOverload() {
        assertEquals("data", TestService.data("data").getData());
        assertEquals(MessageConstant.SUCCESS, TestService.data("data").getMessage());

        PagingRS paging = new PagingRS(0, 10, 1, 1);
        StructureRS paged = TestService.paged("data", paging);
        assertEquals(paging, paged.getPaging());

        StructureRS pagedFromPage = TestService.paged(
                "data", new PageImpl<>(List.of("data"), PageRequest.of(0, 10), 1));
        assertEquals(1, pagedFromPage.getPaging().getTotalElements());

        StructureRS statusOnly = TestService.status(HttpStatus.CREATED, MessageConstant.CREATED);
        assertEquals(201, statusOnly.getStatus());
        assertNull(statusOnly.getData());

        StructureRS statusWithData = TestService.status(HttpStatus.ACCEPTED, "queued", "id-1");
        assertEquals(202, statusWithData.getStatus());
        assertEquals("queued", statusWithData.getMessage());
        assertEquals("id-1", statusWithData.getData());
    }

    @Test
    void baseControllerResponseHelpersCoverEveryOverload() {
        ResponseEntity<StructureRS> raw = TestController.raw(
                new StructureRS(HttpStatus.ACCEPTED, "queued"));
        assertEquals(202, raw.getStatusCode().value());
        assertEquals("queued", raw.getBody().getMessage());

        ResponseEntity<StructureRS> ok = TestController.ok();
        assertEquals(200, ok.getStatusCode().value());
        assertEquals(MessageConstant.SUCCESS, ok.getBody().getMessage());

        ResponseEntity<StructureRS> withData = TestController.ok("payload");
        assertEquals("payload", withData.getBody().getData());

        ResponseEntity<StructureRS> paged = TestController.paged(
                "payload", new PagingRS(0, 10, 1, 1));
        assertEquals(200, paged.getStatusCode().value());
        assertNotNull(paged.getBody().getPaging());

        ResponseEntity<StructureRS> created = TestController.createResponse("payload");
        assertEquals(201, created.getStatusCode().value());
        assertEquals(MessageConstant.CREATED, created.getBody().getMessage());

        ResponseEntity<StructureRS> noContent = TestController.emptyResponse();
        assertEquals(204, noContent.getStatusCode().value());
        assertNull(noContent.getBody());

        ResponseEntity<StructureRS> custom = TestController.custom(
                HttpStatus.CONFLICT, MessageConstant.CONFLICT, "detail");
        assertEquals(409, custom.getStatusCode().value());
        assertEquals("detail", custom.getBody().getData());
    }

    @Test
    void baseEntitySupportsSoftDelete() {
        TestEntity entity = new TestEntity();
        assertFalse(entity.isDeleted());
        assertNull(entity.getDeletedAt());
        assertNull(entity.getUpdatedBy());

        entity.markDeleted("cashier-1");

        assertTrue(entity.isDeleted());
        assertNotNull(entity.getDeletedAt());
        assertEquals("cashier-1", entity.getUpdatedBy());
    }

    @Test
    void sharedConstantsAndEnumsAreAvailableToConsumers() {
        assertEquals("ALL", MessageConstant.ALL);
        assertEquals("Login completed successfully", MessageConstant.Authentication.LOGIN_SUCCESS);
        assertNotNull(MessageConstant.Notification.QUEUED);

        assertEquals(2, CurrencyType.values().length);
        assertEquals(4, OrderStatus.values().length);
        assertEquals(6, PaymentMethod.values().length);
        assertEquals(4, PaymentStatus.values().length);
        assertEquals(2, PromotionType.values().length);
        assertEquals(3, PurchaseStatus.values().length);
        assertEquals(6, RamSize.values().length);
        assertEquals(6, StorageSize.values().length);
        assertEquals(3, UnitType.values().length);
    }
}
