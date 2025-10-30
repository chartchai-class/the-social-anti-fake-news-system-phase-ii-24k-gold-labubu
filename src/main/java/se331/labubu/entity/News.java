package se331.labubu.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    // Calculate fake vote count from comments (excluding deleted comments)
    @Transient
    public long getFakeVoteCount() {
        return comments.stream()
                .filter(c -> !c.getIsDeleted())
                .filter(c -> "fake".equals(c.getVote()))
                .count();
    }

    // Calculate real vote count from comments (excluding deleted comments)
    @Transient
    public long getRealVoteCount() {
        return comments.stream()
                .filter(c -> !c.getIsDeleted())
                .filter(c -> "not-fake".equals(c.getVote()))
                .count();
    }

    // Get total vote count
    @Transient
    public long getTotalVoteCount() {
        return getFakeVoteCount() + getRealVoteCount();
    }

    // Auto-update status based on votes from comments
    public void updateStatus() {
        long fakeCount = getFakeVoteCount();
        long realCount = getRealVoteCount();
        long totalVotes = fakeCount + realCount;

        if (totalVotes == 0) {
            this.status = NewsType.REAL; // Default if no votes
            return;
        }

        // Calculate percentage
        double fakePercentage = (double) fakeCount / totalVotes * 100;

        if (fakePercentage >= 60) {
            this.status = NewsType.FAKE;
        } else if (fakePercentage <= 40) {
            this.status = NewsType.REAL;
        } else {
            // Between 40-60% is uncertain, default to REAL or add UNVERIFIED status
            this.status = NewsType.REAL;
        }
    }
}