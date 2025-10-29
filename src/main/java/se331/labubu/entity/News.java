package se331.labubu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String topic;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String details;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private NewsType status = NewsType.REAL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Vote> votes = new ArrayList<>();

    // Calculate fake vote count (excluding deleted votes)
    @Transient
    public long getFakeVoteCount() {
        return votes.stream()
                .filter(v -> v.getIsFake() && !v.getIsDeleted())
                .count();
    }

    // Calculate real vote count (excluding deleted votes)
    @Transient
    public long getRealVoteCount() {
        return votes.stream()
                .filter(v -> !v.getIsFake() && !v.getIsDeleted())
                .count();
    }

    // Auto-update status based on votes
    public void updateStatus() {
        long fakeCount = getFakeVoteCount();
        long realCount = getRealVoteCount();

        if (fakeCount > realCount) {
            this.status = NewsType.FAKE;
        } else {
            this.status = NewsType.REAL;
        }
    }
}

