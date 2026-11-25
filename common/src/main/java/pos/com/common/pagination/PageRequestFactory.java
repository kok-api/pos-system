package pos.com.common.pagination;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pos.com.common.exception.httpstatus.BadRequestException;

import java.util.Locale;
import java.util.Set;
public final class PageRequestFactory {
    private PageRequestFactory() {
    }
    public static Pageable create(int page, int size, String sort, String order, Set<String> allowedSorts, String defaultSort) {
        String safeSort = normalize(sort);
        if (safeSort == null) {
            safeSort = normalize(defaultSort);
        }
        if (safeSort == null || allowedSorts == null || !allowedSorts.contains(safeSort)) {
            throw new BadRequestException("Unsupported sort field: " + safeSort);
        }
        if (size <= 0) {
            throw new BadRequestException("Page size must be greater than zero");
        }
        Sort.Direction direction;
        try {
            direction = Sort.Direction.fromString(order == null || order.isBlank()
                    ? "DESC"
                    : order.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Sort order must be ASC or DESC");
        }
        return PageRequest.of(Math.max(page - 1, 0), size, Sort.by(direction, safeSort));
    }
    public static String normalize(String value) {
        return value == null || value.isBlank() ? null: value.trim();
    }
}
