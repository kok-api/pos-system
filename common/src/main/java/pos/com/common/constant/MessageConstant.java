package pos.com.common.constant;

/**
 * Centralized user-facing and operational messages shared by POS services.
 *
 * <p>Keep messages here instead of hardcoding them in controllers, services,
 * security handlers, validators, or exception handlers.</p>
 */
public final class MessageConstant {

    private MessageConstant() {
        throw new IllegalStateException(Utility.CLASS_INSTANTIATION_NOT_ALLOWED);
    }

    public static final String SUCCESS = "Request completed successfully";
    public static final String CREATED = "Resource created successfully";
    public static final String UPDATED = "Resource updated successfully";
    public static final String DELETED = "Resource deleted successfully";
    public static final String VALIDATION_FAILED = "Request validation failed";
    public static final String BAD_REQUEST = "Invalid request";
    public static final String UNAUTHORIZED = "Authentication is required";
    public static final String FORBIDDEN = "You do not have permission to perform this action";
    public static final String NOT_FOUND = "Requested resource was not found";
    public static final String CONFLICT = "The request conflicts with the current resource state";
    public static final String INTERNAL_SERVER_ERROR = "An unexpected error occurred";
    public static final String SERVICE_UNAVAILABLE = "Service is temporarily unavailable";
    public static final String ALL = "ALL";

    public static final class Utility {
        private Utility() {
        }

        public static final String CLASS_INSTANTIATION_NOT_ALLOWED = "Utility class cannot be instantiated";
    }

    public static final class Validation {
        private Validation() {
        }

        public static final String USERNAME_REQUIRED = "Username is required";
        public static final String USERNAME_TOO_LONG = "Username must not exceed 100 characters";
        public static final String PASSWORD_REQUIRED = "Password is required";
        public static final String PASSWORD_TOO_LONG = "Password must not exceed 200 characters";
        public static final String REFRESH_TOKEN_REQUIRED = "Refresh token is required";
        public static final String REFRESH_TOKEN_TOO_LONG = "Refresh token must not exceed 500 characters";
    }

    public static final class Configuration {
        private Configuration() {
        }

        public static final String AUTH_LOCK_DURATION_POSITIVE =
                "security.authentication.lock-duration must be positive";
        public static final String JWT_ACCESS_TOKEN_TTL_POSITIVE =
                "security.jwt.access-token-ttl must be positive";
        public static final String JWT_REFRESH_TOKEN_TTL_POSITIVE =
                "security.jwt.refresh-token-ttl must be positive";
        public static final String JWT_CLOCK_SKEW_NON_NEGATIVE =
                "security.jwt.clock-skew must not be negative";
        public static final String JWT_SECRET_MINIMUM_LENGTH =
                "security.jwt.secret-base64 must decode to at least 32 bytes";
    }

    public static final class Security {
        private Security() {
        }

        public static final String SHA_256_UNAVAILABLE = "SHA-256 is not available";
    }

    public static final class Authentication {
        private Authentication() {
        }

        public static final String LOGIN_SUCCESS = "Login completed successfully";
        public static final String REFRESH_SUCCESS = "Access token refreshed successfully";
        public static final String LOGOUT_SUCCESS = "Logout completed successfully";
        public static final String CURRENT_USER_RETRIEVED = "Current user retrieved successfully";
        public static final String TOKEN_VALID = "Token is valid";

        public static final String INVALID_CREDENTIALS = "Invalid username or password";
        public static final String ACCOUNT_LOCKED = "Account is temporarily locked";
        public static final String ACCOUNT_DEACTIVATED = "User account has been deactivated";
        public static final String REFRESH_TOKEN_INVALID_OR_EXPIRED = "Refresh token is invalid or expired";
        public static final String TOKEN_EXPIRED = "Authentication token has expired";
        public static final String TOKEN_INVALID = "Authentication token is invalid";
        public static final String AUTHENTICATION_REQUIRED_OR_TOKEN_INVALID =
                "Authentication is required or the token is invalid";
        public static final String ACCESS_DENIED = "You do not have permission to access this resource";
        public static final String REQUIRED_AUDIENCE_MISSING = "Required audience is missing";
        public static final String INVALID_TOKEN_TYPE = "Invalid token type";
    }

    public static final class Audit {
        private Audit() {
        }

        public static final String RECORDED = "Audit event recorded successfully";
        public static final String RETRIEVED = "Audit event retrieved successfully";
        public static final String LIST_RETRIEVED = "Audit events retrieved successfully";
        public static final String NOT_FOUND = "Audit event was not found";
    }

    public static final class AuditValidation {
        private AuditValidation() {
        }

        public static final String ACTION_REQUIRED = "Audit action is required";
        public static final String ACTION_TOO_LONG = "Audit action must not exceed 100 characters";
        public static final String JSON_INVALID = "Audit JSON value is invalid";
    }

    public static final class Notification {
        private Notification() {
        }

        public static final String TEMPLATE_CREATED = "Notification template created successfully";
        public static final String TEMPLATE_RETRIEVED = "Notification template retrieved successfully";
        public static final String TEMPLATES_RETRIEVED = "Notification templates retrieved successfully";
        public static final String TEMPLATE_UPDATED = "Notification template updated successfully";
        public static final String TEMPLATE_STATUS_UPDATED = "Notification template status updated successfully";
        public static final String QUEUED = "Notification queued successfully";
        public static final String RETRIEVED = "Notification retrieved successfully";
        public static final String LIST_RETRIEVED = "Notifications retrieved successfully";
        public static final String PROCESSING = "Notification marked as processing";
        public static final String SENT = "Notification marked as sent";
        public static final String FAILED = "Notification marked as failed";
        public static final String RETRIED = "Notification queued for retry";
        public static final String CANCELLED = "Notification cancelled successfully";
        public static final String TEMPLATE_NOT_FOUND = "Notification template was not found";
        public static final String NOT_FOUND = "Notification was not found";
        public static final String TEMPLATE_CODE_EXISTS = "Notification template code already exists";
        public static final String TEMPLATE_INACTIVE = "Notification template is inactive";
        public static final String INVALID_STATUS_TRANSITION = "Notification status transition is not allowed";
        public static final String SCHEDULED_IN_FUTURE = "Notification is scheduled for a future time";
        public static final String MISSING_TEMPLATE_VARIABLE = "Required template variable is missing: ";
        public static final String SUBJECT_TOO_LONG =
                "Rendered notification subject must not exceed 200 characters";
        public static final String BODY_TOO_LONG =
                "Rendered notification message must not exceed 2000 characters";
    }

    public static final class NotificationValidation {
        private NotificationValidation() {
        }

        public static final String CODE_REQUIRED = "Template code is required";
        public static final String CHANNEL_REQUIRED = "Notification channel is required";
        public static final String BODY_REQUIRED = "Notification message is required";
        public static final String RECIPIENT_REQUIRED = "Notification recipient is required";
        public static final String VARIABLE_KEY_INVALID = "Template variable key is invalid";
    }

    public static final class User {
        private User() {
        }

        public static final String CREATED = "User created successfully";
        public static final String RETRIEVED = "User retrieved successfully";
        public static final String LIST_RETRIEVED = "Users retrieved successfully";
        public static final String UPDATED = "User updated successfully";
        public static final String DELETED = "User deleted successfully";
        public static final String NOT_FOUND = "User was not found";
        public static final String EMAIL_ALREADY_EXISTS = "An account with this email already exists";
        public static final String PASSWORD_UPDATED = "Password updated successfully";
        public static final String CURRENT_PASSWORD_INVALID = "Current password is invalid";
        public static final String ROLE_ASSIGNED = "Role assigned successfully";
    }

    public static final class UserValidation {
        private UserValidation() {
        }

        public static final String EMAIL_REQUIRED = "Email is required";
        public static final String EMAIL_INVALID = "Email format is invalid";
        public static final String PASSWORD_TOO_SHORT = "Password must be at least 8 characters";
        public static final String NAME_REQUIRED = "Name is required";
    }

    public static final class UserAddress {
        private UserAddress() {
        }

        public static final String CREATED = "Address added successfully";
        public static final String UPDATED = "Address updated successfully";
        public static final String DELETED = "Address removed successfully";
        public static final String LIST_RETRIEVED = "Addresses retrieved successfully";
        public static final String NOT_FOUND = "Address was not found";
        public static final String NOT_OWNED_BY_USER = "Address does not belong to this user";
    }

    public static final class UserVerification {
        private UserVerification() {
        }

        public static final String CODE_SENT = "Verification code sent successfully";
        public static final String VERIFIED = "Account verified successfully";
        public static final String CODE_EXPIRED = "Verification code has expired";
        public static final String CODE_INVALID = "Verification code is invalid";
        public static final String ACCOUNT_NOT_VERIFIED = "Account is not verified";
    }

    public static final class Role {
        private Role() {
        }

        public static final String CREATED = "Role created successfully";
        public static final String UPDATED = "Role updated successfully";
        public static final String DELETED = "Role deleted successfully";
        public static final String LIST_RETRIEVED = "Roles retrieved successfully";
        public static final String NOT_FOUND = "Role was not found";
        public static final String IN_USE = "Role cannot be deleted while still assigned to users";
        public static final String PERMISSIONS_ASSIGNED = "Permissions assigned successfully";
        public static final String PERMISSION_REVOKED = "Permission revoked successfully";
    }

    public static final class Permission {
        private Permission() {
        }

        public static final String CREATED = "Permission created successfully";
        public static final String UPDATED = "Permission updated successfully";
        public static final String DELETED = "Permission deleted successfully";
        public static final String LIST_RETRIEVED = "Permissions retrieved successfully";
        public static final String NOT_FOUND = "Permission was not found";
        public static final String IN_USE = "Permission cannot be deleted while still assigned to a role";
    }

    public static final class Device {
        private Device() {
        }

        public static final String REGISTERED = "Device registered successfully";
        public static final String UPDATED = "Device updated successfully";
        public static final String UNREGISTERED = "Device unregistered successfully";
        public static final String LIST_RETRIEVED = "Devices retrieved successfully";
        public static final String NOT_FOUND = "Device was not found";
    }

    public static final class Loyalty {
        private Loyalty() {
        }

        public static final String POINTS_EARNED = "Loyalty points earned successfully";
        public static final String POINTS_REDEEMED = "Loyalty points redeemed successfully";
        public static final String BALANCE_RETRIEVED = "Loyalty balance retrieved successfully";
        public static final String HISTORY_RETRIEVED = "Loyalty history retrieved successfully";
        public static final String ACCOUNT_NOT_FOUND = "Loyalty account was not found";
        public static final String INSUFFICIENT_POINTS = "Redemption amount exceeds the available points balance";
    }

    public static final class Category {
        private Category() {
        }

        public static final String CREATED = "Category created successfully";
        public static final String UPDATED = "Category updated successfully";
        public static final String DELETED = "Category deleted successfully";
        public static final String LIST_RETRIEVED = "Categories retrieved successfully";
        public static final String NOT_FOUND = "Category was not found";
    }

    public static final class Supplier {
        private Supplier() {
        }

        public static final String CREATED = "Supplier created successfully";
        public static final String UPDATED = "Supplier updated successfully";
        public static final String ACTIVATED = "Supplier activated successfully";
        public static final String DEACTIVATED = "Supplier deactivated successfully";
        public static final String LIST_RETRIEVED = "Suppliers retrieved successfully";
        public static final String NOT_FOUND = "Supplier was not found";
    }

    public static final class FileMedia {
        private FileMedia() {
        }

        public static final String UPLOADED = "File uploaded successfully";
        public static final String DELETED = "File deleted successfully";
        public static final String LIST_RETRIEVED = "Files retrieved successfully";
        public static final String NOT_FOUND = "File was not found";
    }

    public static final class Product {
        private Product() {
        }

        public static final String CREATED = "Product created successfully";
        public static final String RETRIEVED = "Product retrieved successfully";
        public static final String LIST_RETRIEVED = "Products retrieved successfully";
        public static final String UPDATED = "Product updated successfully";
        public static final String ACTIVATED = "Product activated successfully";
        public static final String DEACTIVATED = "Product deactivated successfully";
        public static final String NOT_FOUND = "Product was not found";
        public static final String CATEGORY_REQUIRED = "Product must belong to a category";
        public static final String OPTION_ADDED = "Product option added successfully";
        public static final String OPTION_CODE_EXISTS = "Product option code already exists";
    }

    public static final class Stock {
        private Stock() {
        }

        public static final String RESERVED = "Stock reserved successfully";
        public static final String RELEASED = "Stock released successfully";
        public static final String RECEIVED = "Stock received successfully";
        public static final String INSUFFICIENT = "Insufficient stock for the requested quantity";
        public static final String OPTION_NOT_FOUND = "Product option was not found";
    }

    public static final class Promotion {
        private Promotion() {
        }

        public static final String CREATED = "Promotion created successfully";
        public static final String UPDATED = "Promotion updated successfully";
        public static final String LIST_RETRIEVED = "Promotions retrieved successfully";
        public static final String NOT_FOUND = "Promotion was not found";
        public static final String INVALID_DATE_RANGE = "Promotion start date must be before its end date";
        public static final String OPTION_REFERENCE_INVALID = "One or more referenced product options do not exist";
    }

    public static final class ExchangeRate {
        private ExchangeRate() {
        }

        public static final String UPDATED = "Exchange rate updated successfully";
        public static final String LIST_RETRIEVED = "Exchange rates retrieved successfully";
        public static final String NOT_FOUND = "No active exchange rate was found for this currency pair";
    }

    public static final class Report {
        private Report() {
        }

        public static final String SALES_SUMMARY_RETRIEVED = "Sales summary retrieved successfully";
        public static final String LOW_STOCK_RETRIEVED = "Low stock report retrieved successfully";
    }

    public static final class Cart {
        private Cart() {
        }

        public static final String ITEM_ADDED = "Item added to cart successfully";
        public static final String ITEM_UPDATED = "Cart item updated successfully";
        public static final String ITEM_REMOVED = "Cart item removed successfully";
        public static final String RETRIEVED = "Cart retrieved successfully";
        public static final String CLEARED = "Cart cleared successfully";
        public static final String EMPTY = "Cart is empty";
    }

    public static final class Order {
        private Order() {
        }

        public static final String CHECKED_OUT = "Order placed successfully";
        public static final String RETRIEVED = "Order retrieved successfully";
        public static final String LIST_RETRIEVED = "Orders retrieved successfully";
        public static final String STATUS_UPDATED = "Order status updated successfully";
        public static final String DISCOUNT_APPLIED = "Invoice discount applied successfully";
        public static final String NOT_FOUND = "Order was not found";
        public static final String INVALID_STATUS_TRANSITION = "Order status transition is not allowed";
        public static final String DISCOUNT_ONLY_WHILE_PENDING =
                "Invoice discount can only be applied while the order is pending";
    }

    public static final class Payment {
        private Payment() {
        }

        public static final String INITIATED = "Payment initiated successfully";
        public static final String MARKED_PAID = "Payment marked as paid successfully";
        public static final String MARKED_REFUNDED = "Payment marked as refunded successfully";
        public static final String NOT_FOUND = "Payment was not found";
        public static final String NOT_PAID = "Payment must be paid before it can be refunded";
    }

    public static final class Refund {
        private Refund() {
        }

        public static final String PROCESSED = "Refund processed successfully";
        public static final String AMOUNT_CALCULATED = "Refund amount calculated successfully";
        public static final String QUANTITY_EXCEEDS_REMAINING =
                "Refund quantity exceeds the remaining refundable quantity";
        public static final String ITEM_NOT_FOUND = "Order item was not found";
    }

    public static final class Shift {
        private Shift() {
        }

        public static final String OPENED = "Shift opened successfully";
        public static final String CLOSED = "Shift closed successfully";
        public static final String SALE_RECORDED = "Cash sale recorded successfully";
        public static final String ALREADY_OPEN = "Cashier already has an open shift";
        public static final String NOT_FOUND = "Open shift was not found for this cashier";
    }

    public static final class Tax {
        private Tax() {
        }

        public static final String CREATED = "Tax configuration created successfully";
        public static final String UPDATED = "Tax configuration updated successfully";
        public static final String LIST_RETRIEVED = "Tax configurations retrieved successfully";
        public static final String NOT_FOUND = "Tax configuration was not found";
    }

    public static final class Purchase {
        private Purchase() {
        }

        public static final String CREATED = "Purchase order created successfully";
        public static final String COMPLETED = "Purchase order completed successfully";
        public static final String CANCELLED = "Purchase order cancelled successfully";
        public static final String RETRIEVED = "Purchase order retrieved successfully";
        public static final String LIST_RETRIEVED = "Purchase orders retrieved successfully";
        public static final String NOT_FOUND = "Purchase order was not found";
        public static final String NOT_PENDING = "Purchase order must be pending for this operation";
    }

    public static final class Inventory {
        private Inventory() {
        }

        public static final String RETRIEVED = "Goods-received record retrieved successfully";
        public static final String LIST_RETRIEVED = "Goods-received records retrieved successfully";
        public static final String NOT_FOUND = "Goods-received record was not found";
    }

    public static final class InventoryMoment {
        private InventoryMoment() {
        }

        public static final String RECORDED = "Stock-out snapshot recorded successfully";
        public static final String ALREADY_RECORDED = "A stock-out snapshot already exists for this order";
        public static final String NOT_FOUND = "Stock-out snapshot was not found";
    }

    public static final class StockAdjustment {
        private StockAdjustment() {
        }

        public static final String RECORDED = "Stock adjustment recorded successfully";
        public static final String LIST_RETRIEVED = "Stock adjustments retrieved successfully";
        public static final String NOT_FOUND = "Stock adjustment was not found";
        public static final String REASON_REQUIRED = "Stock adjustment reason is required";
    }
}
