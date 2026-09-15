package com.library.lms.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Phiếu mượn sách - liên kết giữa User và Book, theo dõi trạng thái mượn/trả.
 */
@Entity
@Table(name = "borrow_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private BorrowStatus status = BorrowStatus.BORROWED;

    @PrePersist
    public void prePersist() {
        if (this.borrowDate == null) {
            this.borrowDate = LocalDate.now();
        }
        if (this.dueDate == null) {
            this.dueDate = this.borrowDate.plusDays(14); // Mặc định mượn 14 ngày
        }
    }
}
