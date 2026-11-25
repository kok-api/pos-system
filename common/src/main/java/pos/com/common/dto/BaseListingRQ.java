package pos.com.common.dto;

import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pos.com.common.pagination.PageRequestFactory;

import java.util.Set;

@Setter
public class BaseListingRQ {
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;
    private static final String DEFAULT_SORT = "id";

    private Integer page;
    private Integer size;
    private String query;
    private String sort;
    private String order;

    public int getPage() { return page == null || page <= 0 ? 0 : page - 1; }
    public int getSize() { return size == null ? DEFAULT_SIZE : Math.min(Math.max(size, 1), MAX_SIZE); }
    public String getQuery() { return query == null || query.isBlank() ? "ALL" : query.trim(); }
    public boolean hasQuery() { return !"ALL".equalsIgnoreCase(getQuery()); }
    public String getSort() { return sort == null || sort.isBlank() ? DEFAULT_SORT : sort.trim(); }
    public String getOrder() {
        return "ASC".equalsIgnoreCase(order) ? "ASC" : "DESC";
    }

    public Pageable getPageable() { return getPageable(getSort(), getOrder()); }
    public Pageable getPageable(String column) { return getPageable(column, getOrder()); }
    public Pageable getPageable(String column, String direction) {
        String safeColumn = column == null || column.isBlank() ? DEFAULT_SORT : column.trim();
        Sort.Direction safeDirection = "ASC".equalsIgnoreCase(direction)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        return PageRequest.of(getPage(), getSize(), Sort.by(safeDirection, safeColumn));
    }

    public Pageable getPageable(Set<String> allowedSorts, String defaultSort) {
        return PageRequestFactory.create(
                getPage() + 1, getSize(), getSort(), getOrder(), allowedSorts, defaultSort);
    }
}
