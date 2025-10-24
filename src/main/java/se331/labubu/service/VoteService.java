package se331.labubu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import se331.labubu.dto.VoteDTO;
import se331.labubu.entity.User;

@Service
@RequiredArgsConstructor
public interface VoteService {
    public Page<VoteDTO> getVotesByNewsId(Long newsId, PageRequest of, User currentUser) {
    }

    public VoteDTO submitVote(Long newsId, VoteRequest request, User currentUser) {
    }
}
