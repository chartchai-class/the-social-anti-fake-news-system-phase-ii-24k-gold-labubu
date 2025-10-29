package se331.labubu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se331.labubu.dto.NewsDTO;
import se331.labubu.dto.NewsRequest;
import se331.labubu.entity.News;
import se331.labubu.entity.NewsType;
import se331.labubu.entity.Role;
import se331.labubu.entity.User;
import se331.labubu.repository.NewsRepository;
import se331.labubu.util.LabMapper;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    //Get news
    @Override
    public Page<NewsDTO> getNews(Pageable pageable, String status, String search, User currentUser) {
        Page<News> newsPage;

        // Admin can see deleted news
        boolean isAdmin = currentUser != null && currentUser.getRole() == Role.ADMIN;

        if (search != null && !search.trim().isEmpty()) {
            // Search with optional status filter
            if (status != null && !status.trim().isEmpty()) {
                NewsType newsStatus = NewsType.valueOf(status.toUpperCase());
                newsPage = newsRepository.searchNewsByStatus(search, newsStatus, pageable);
            } else {
                newsPage = newsRepository.searchNews(search, pageable);
            }
        } else if (status != null && !status.trim().isEmpty()) {
            // Filter by status only
            NewsType newsStatus = NewsType.valueOf(status.toUpperCase());
            newsPage = newsRepository.findByStatusAndIsDeletedFalse(newsStatus, pageable);
        } else {
            // Show all news
            if (isAdmin) {
                newsPage = newsRepository.findAll(pageable);
            } else {
                newsPage = newsRepository.findByIsDeletedFalse(pageable);
            }
        }

        return newsPage.map(news -> {
            NewsDTO dto = LabMapper.INSTANCE.getNewsDTO(news);
            dto.setFakeVoteCount(news.getFakeVoteCount());
            dto.setRealVoteCount(news.getRealVoteCount());
            return dto;
        });
    }

    //Get news by ID
    @Override
    public NewsDTO getNewsById(Long newsId, User currentUser) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new IllegalArgumentException("News not found"));

        // Check if deleted and user is not admin
        if (news.getIsDeleted() &&
                (currentUser == null || currentUser.getRole() != Role.ADMIN)) {
            throw new IllegalArgumentException("News not found");
        }

        NewsDTO dto = LabMapper.INSTANCE.getNewsDTO(news);
        dto.setFakeVoteCount(news.getFakeVoteCount());
        dto.setRealVoteCount(news.getRealVoteCount());

        return dto;
    }

    //Create news
    @Override
    @Transactional
    public NewsDTO createNews(NewsRequest request, User currentUser) {
        // Only MEMBER and ADMIN can create news
        if (currentUser.getRole() != Role.MEMBER && currentUser.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("Only members can submit news");
        }

        News news = News.builder()
                .topic(request.getTopic())
                .details(request.getDetails())
                .imageUrl(request.getImageUrl())
                .reporter(currentUser)
                .status(NewsType.REAL)
                .isDeleted(false)
                .build();

        newsRepository.save(news);

        return LabMapper.INSTANCE.getNewsDTO(news);
    }

    //Delete news
    @Override
    @Transactional
    public void softDeleteNews(Long newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new IllegalArgumentException("News not found"));

        news.setIsDeleted(true);
        newsRepository.save(news);
    }

    @Override
    public NewsDTO restoreNews(Long newsId) {
        return null;
    }
}
