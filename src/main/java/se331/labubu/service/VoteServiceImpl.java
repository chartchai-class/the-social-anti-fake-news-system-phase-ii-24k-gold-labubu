package se331.labubu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se331.labubu.dto.VoteDTO;
import se331.labubu.dto.VoteRequest;
import se331.labubu.entity.News;
import se331.labubu.entity.Role;
import se331.labubu.entity.User;
import se331.labubu.entity.Vote;
import se331.labubu.repository.NewsRepository;
import se331.labubu.repository.VoteRepository;
import se331.labubu.util.LabMapper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final NewsRepository newsRepository;

    @Override
    public Page<VoteDTO> getVotesByNewsId(Long newsId, Pageable pageable, User currentUser) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new IllegalArgumentException("News not found"));

        // Admin can see deleted votes
        Page<Vote> votes;
        if (currentUser != null && currentUser.getRole() == Role.ADMIN) {
            votes = voteRepository.findByNews(news, pageable);
        } else {
            votes = voteRepository.findByNewsAndIsDeletedFalse(news, pageable);
        }

        return votes.map(LabMapper.INSTANCE::getVoteDTO);
    }

    @Override
    @Transactional
    public VoteDTO submitVote(Long newsId, VoteRequest request, User currentUser) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new IllegalArgumentException("News not found"));

        // Check if news is deleted
        if (news.getIsDeleted()) {
            throw new IllegalArgumentException("Cannot vote on deleted news");
        }

        // Check if user already voted
        Optional<Vote> existingVote = voteRepository.findByNewsAndUser(news, currentUser);

        Vote vote;
        if (existingVote.isPresent()) {
            // Update existing vote
            vote = existingVote.get();
            vote.setIsFake(request.getIsFake());
            vote.setComment(request.getComment());
            vote.setImageUrl(request.getImageUrl());
        } else {
            // Create new vote
            vote = Vote.builder()
                    .news(news)
                    .user(currentUser)
                    .isFake(request.getIsFake())
                    .comment(request.getComment())
                    .imageUrl(request.getImageUrl())
                    .isDeleted(false)
                    .build();
        }

        voteRepository.save(vote);

        // Update news status based on votes
        news.updateStatus();
        newsRepository.save(news);

        return LabMapper.INSTANCE.getVoteDTO(vote);
    }
}