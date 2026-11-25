package pos.com.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Base class for every JPA entity across the POS services.
 *
 * <p>Owns the identifier and the full audit trail (created/updated at + by), plus
 * soft-delete via {@code deletedAt}. Entities are never hard-deleted: callers use
 * {@link #markDeleted(String)} and filter on {@link #isDeleted()}.</p>
 *
 * <p>{@code createdAt}/{@code updatedAt} populate via Spring Data JPA auditing
 * ({@link AuditingEntityListener}) — the consuming service must enable it with
 * {@code @EnableJpaAuditing}. {@code createdBy}/{@code updatedBy} additionally
 * need an {@code AuditorAware} bean registered; without one they stay {@code null}.</p>
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void markDeleted(String deletedBy) {
        // no separate deletedBy column: deletion is recorded as an update
        this.deletedAt = LocalDateTime.now();
        this.updatedBy = deletedBy;
    }
}
