package se331.labubu.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import se331.labubu.dao.TokenDao;
import se331.labubu.entity.Token;
import se331.labubu.repository.TokenRepository;

@Repository
@RequiredArgsConstructor
public class TokenDaoImpl implements TokenDao {
    final TokenRepository tokenRepository;

    @Override
    public void save(Token token) {
        tokenRepository.save(token);
    }
}