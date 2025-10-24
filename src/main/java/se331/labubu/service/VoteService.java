package se331.labubu.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se331.labubu.dto.VoteDTO;
import se331.labubu.dto.VoteRequest;
import se331.labubu.entity.User;

public interface VoteService {
    Page<VoteDTO> getVotesByNewsId(Long newsId, Pageable pageable, User currentUser);
    VoteDTO submitVote(Long newsId, VoteRequest request, User currentUser);
}