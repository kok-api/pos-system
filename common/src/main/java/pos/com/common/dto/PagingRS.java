package pos.com.common.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PagingRS {
    private final int page;
    private final int size;
    private final int totalPages;
    private final long totalElements;

    public PagingRS(int page, int size, int totalPages, long totalElements) {
        this.page = Math.max(page, 0);
        this.size = Math.max(size, 0);
        this.totalPages = Math.max(totalPages, 0);
        this.totalElements = Math.max(totalElements, 0);
    }

    public PagingRS(Page<?> page) {
        this(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
    }
}
