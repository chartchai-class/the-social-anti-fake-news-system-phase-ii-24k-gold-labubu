package se331.labubu.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se331.labubu.dto.NewsDTO;
import se331.labubu.dto.NewsRequest;
import se331.labubu.entity.User;

public interface NewsService {
    Page<NewsDTO> getNews(Pageable pageable, String status, String search, User currentUser);
    NewsDTO getNewsById(Long newsId, User currentUser);
    NewsDTO createNews(NewsRequest request, User currentUser);
    void softDeleteNews(Long newsId);
    NewsDTO restoreNews(Long newsId);
}