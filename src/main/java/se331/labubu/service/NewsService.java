package se331.labubu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import se331.labubu.dto.NewsDTO;
import se331.labubu.entity.News;
import se331.labubu.entity.User;

@Service
@RequiredArgsConstructor
public interface NewsService {
    Page<News> getNews(Integer pageSize, Integer page);
    News getNewsById(Long id);


//    public Page<NewsDTO> getNews(PageRequest of, String status, String search, User currentUser) {
//    }

    public NewsDTO getNewsById(Long id, User currentUser) {

    }
}
