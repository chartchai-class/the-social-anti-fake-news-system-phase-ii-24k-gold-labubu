package se331.labubu.config;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import se331.labubu.entity.*;
import se331.labubu.repository.*;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InitApp implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {

        // Clear existing data
        commentRepository.deleteAll();
        newsRepository.deleteAll();
        userRepository.deleteAll();

        System.out.println("🚀 Initializing Anti-Fake News System data...");

        // 1. Create Users with different roles
        User admin = createUser(
                "Admin", "User",
                "admin@antifakenews.com",
                "admin123",
                Role.ADMIN,
                "https://i.pravatar.cc/150?img=1",
                "adminUser"
        );

        User member1 = createUser(
                "John", "Doe",
                "john@example.com",
                "password123",
                Role.MEMBER,
                "https://i.pravatar.cc/150?img=2",
                "john_doe"
        );

        User member2 = createUser(
                "Jane", "Smith",
                "jane@example.com",
                "password123",
                Role.MEMBER,
                "https://i.pravatar.cc/150?img=3",
                "jane_smith"
        );

        User reader1 = createUser(
                "Bob", "Johnson",
                "bob@example.com",
                "password123",
                Role.READER,
                "https://i.pravatar.cc/150?img=4",
                "bob_johnson"
        );

        User reader2 = createUser(
                "Alice", "Brown",
                "alice@example.com",
                "password123",
                Role.READER,
                "https://i.pravatar.cc/150?img=5",
                "alice_brown"
        );

        User reader3 = createUser(
                "Charlie", "Wilson",
                "charlie@example.com",
                "password123",
                Role.READER,
                "https://i.pravatar.cc/150?img=6",
                "charlie_wilson"
        );

        // 2. Create News Articles
        News news1 = createNews(
                "Breaking: New AI Technology Revolutionizes Healthcare",
                "Scientists have developed a groundbreaking AI system that can diagnose diseases with 99% accuracy. The system has been tested in multiple hospitals and shows promising results.",
                member1,
                "https://images.unsplash.com/photo-1576091160399-112ba8d25d1d?w=800",
                NewsType.REAL,
                LocalDateTime.now().minusDays(5)
        );

        News news2 = createNews(
                "FAKE ALERT: Government Plans to Ban All Social Media",
                "According to unverified sources, the government is planning to shut down all social media platforms next month. This claim has been widely circulated but lacks credible evidence.",
                member2,
                "https://images.unsplash.com/photo-1611162617474-5b21e879e113?w=800",
                NewsType.FAKE,
                LocalDateTime.now().minusDays(4)
        );

        News news3 = createNews(
                "Local University Wins International Research Award",
                "The university's research team has been awarded a prestigious international prize for their work on renewable energy. The ceremony will be held next month.",
                member1,
                "https://images.unsplash.com/photo-1523050854058-8df90110c9f1?w=800",
                NewsType.REAL,
                LocalDateTime.now().minusDays(3)
        );

        News news4 = createNews(
                "UNVERIFIED: Miracle Cure for All Diseases Discovered",
                "A viral post claims that a simple home remedy can cure all diseases. Medical experts warn this is dangerous misinformation with no scientific backing.",
                member2,
                "https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?w=800",
                NewsType.FAKE,
                LocalDateTime.now().minusDays(2)
        );

        News news5 = createNews(
                "New Public Transportation System Launches Next Week",
                "The city announces the launch of a modern electric bus system starting next Monday. Routes and schedules have been published on the official website.",
                member1,
                "https://images.unsplash.com/photo-1570125909232-eb263c188f7e?w=800",
                NewsType.REAL,
                LocalDateTime.now().minusDays(1)
        );

        News news6 = createNews(
                "FAKE: Celebrity Dies in Car Crash - Completely False",
                "A hoax claiming a famous celebrity died in a car crash has been spreading on social media. The celebrity has confirmed they are alive and well.",
                member2,
                "https://images.unsplash.com/photo-1533073526757-2c8ca1df9f1c?w=800",
                NewsType.FAKE,
                LocalDateTime.now().minusHours(12)
        );

        News news7 = createNews(
                "Weather Alert: Storm Expected This Weekend",
                "Meteorological department warns of heavy rainfall and strong winds this weekend. Residents are advised to take necessary precautions.",
                member1,
                "https://images.unsplash.com/photo-1527482797697-8795b05a13fe?w=800",
                NewsType.REAL,
                LocalDateTime.now().minusHours(6)
        );

        News news8 = createNews(
                "UFO Spotted Over City",
                "Multiple residents report seeing unusual lights in the sky last night. Authorities are investigating the claims. No official confirmation yet.",
                member2,
                "https://images.unsplash.com/photo-1614730321146-b6fa6a46bcb4?w=800",
                NewsType.REAL,
                LocalDateTime.now().minusHours(2)
        );

        // 3. Create Comments with Votes (combined - matching db.json structure)

        // News 1 (REAL) - More "not-fake" votes through comments
        createCommentWithVote(news1, reader1, "This is well-documented. I saw this on multiple credible news sites.", "not-fake", null);
        createCommentWithVote(news1, reader2, "My doctor mentioned this technology. It's real.", "not-fake", null);
        createCommentWithVote(news1, member2, "Verified with the research institution.", "not-fake", null);
        createCommentWithVote(news1, reader3, "Sounds too good to be true.", "fake", null);
        createCommentWithVote(news1, reader1, "This is amazing news for healthcare!", "not-fake", null);
        createCommentWithVote(news1, member2, "Can't wait to see this implemented in our hospitals.", "not-fake", null);

        // News 2 (FAKE) - More "fake" votes through comments
        createCommentWithVote(news2, reader1, "No official government announcement. This is clearly fake.", "fake", null);
        createCommentWithVote(news2, reader2, "Checked official channels. Nothing there. Fake news!", "fake", null);
        createCommentWithVote(news2, reader3, "Classic misinformation tactic.", "fake", null);
        createCommentWithVote(news2, member1, "Could be real, governments do this.", "not-fake", null);
        createCommentWithVote(news2, reader2, "Please verify before sharing such claims!", "fake", null);
        createCommentWithVote(news2, reader3, "This kind of fake news creates unnecessary panic.", "fake", null);

        // News 3 (REAL) - More "not-fake" votes through comments
        createCommentWithVote(news3, reader1, "I attended the ceremony. It's real!", "not-fake", null);
        createCommentWithVote(news3, reader2, "Official university announcement confirms this.", "not-fake", null);
        createCommentWithVote(news3, reader3, "Great news for our university!", "not-fake", null);
        createCommentWithVote(news3, member1, "Proud of our research team!", "not-fake", null);
        createCommentWithVote(news3, reader1, "Well deserved recognition!", "not-fake", null);

        // News 4 (FAKE) - More "fake" votes through comments
        createCommentWithVote(news4, reader1, "Medical professionals debunked this. Dangerous misinformation!", "fake", null);
        createCommentWithVote(news4, reader2, "No scientific evidence. Definitely fake.", "fake", null);
        createCommentWithVote(news4, member1, "This could harm people. Should be removed.", "fake", null);
        createCommentWithVote(news4, reader3, "Snake oil claims. Pure fake.", "fake", null);
        createCommentWithVote(news4, reader1, "This is dangerous misinformation that could harm people.", "fake", null);
        createCommentWithVote(news4, admin, "We're monitoring this closely. Thank you for reporting.", "fake", null);

        // News 5 (REAL) - More "not-fake" votes through comments
        createCommentWithVote(news5, reader1, "Saw the buses being prepared. It's happening!", "not-fake", null);
        createCommentWithVote(news5, reader2, "Official announcement on city website.", "not-fake", null);
        createCommentWithVote(news5, reader3, "Finally! Been waiting for this.", "not-fake", null);
        createCommentWithVote(news5, reader2, "Finally some good infrastructure news!", "not-fake", null);
        createCommentWithVote(news5, reader3, "What are the new bus routes?", "not-fake", null);

        // News 6 (FAKE) - More "fake" votes through comments
        createCommentWithVote(news6, reader1, "Celebrity posted on their social media. They're alive. This is fake!", "fake", null);
        createCommentWithVote(news6, reader2, "Hoax confirmed by multiple fact-checkers.", "fake", null);
        createCommentWithVote(news6, member2, "Typical death hoax. Happens all the time.", "fake", null);

        // News 7 (REAL) - More "not-fake" votes through comments
        createCommentWithVote(news7, reader1, "Official weather bureau warning. Take it seriously!", "not-fake", null);
        createCommentWithVote(news7, reader2, "Weather apps confirm the storm. Real!", "not-fake", null);

        // News 8 (MIXED) - Mixed votes through comments
        createCommentWithVote(news8, reader1, "Probably just drones or planes.", "fake", null);
        createCommentWithVote(news8, reader2, "I saw it too! Something strange was definitely there.", "not-fake", null);
        createCommentWithVote(news8, reader1, "Need more evidence before making conclusions.", "fake", null);
        createCommentWithVote(news8, reader2, "I have photos! How can I share them?", "not-fake", null);

        // Print summary
        System.out.println("✅ Data initialized successfully!");
        System.out.println("📊 Created " + userRepository.count() + " users");
        System.out.println("   - 1 Admin, 2 Members, 3 Readers");
        System.out.println("📰 Created " + newsRepository.count() + " news articles");
        System.out.println("💬 Created " + commentRepository.count() + " comments (with votes)");
        System.out.println("\n📋 Sample Login Credentials:");
        System.out.println("   Admin: admin@antifakenews.com / admin123");
        System.out.println("   Member: john@example.com / password123");
        System.out.println("   Reader: bob@example.com / password123");
    }

    private User createUser(String name, String surname, String email,
                            String password, Role role, String profileImage, String username) {
        User user = User.builder()
                .name(name)
                .surname(surname)
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(role)
                .profileImage(profileImage)
                .build();
        return userRepository.save(user);
    }

    private News createNews(String topic, String details, User reporter,
                            String imageUrl, NewsType status, LocalDateTime createdAt) {
        News news = News.builder()
                .topic(topic)
                .details(details)
                .reporter(reporter)
                .imageUrl(imageUrl)
                .status(status)
                .createdAt(createdAt)
                .isDeleted(false)
                .build();
        return newsRepository.save(news);
    }

    // NEW METHOD: Create comment with vote combined (matches db.json structure)
    private Comment createCommentWithVote(News news, User user, String content,
                                          String vote, String imageUrl) {
        Comment comment = Comment.builder()
                .news(news)
                .user(user)
                .content(content)
                .vote(vote) // "fake" or "not-fake"
                .imageUrl(imageUrl)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .build();

        Comment savedComment = commentRepository.save(comment);

        // Update news status based on all comments' votes
        news.updateStatus();
        newsRepository.save(news);

        return savedComment;
    }
}