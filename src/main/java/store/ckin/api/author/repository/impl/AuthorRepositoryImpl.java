package store.ckin.api.author.repository.impl;

import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.ckin.api.author.dto.response.AuthorResponseDto;
import store.ckin.api.author.entity.Author;
import store.ckin.api.author.entity.QAuthor;
import store.ckin.api.author.repository.AuthorRepositoryCustom;

/**
 * AuthorRepositoryImpl.
 *
 * @author 나국로
 * @version 2024. 02. 15.
 */
public class AuthorRepositoryImpl extends QuerydslRepositorySupport implements AuthorRepositoryCustom {

    /**
     * Instantiates a new Author repository.
     */
    public AuthorRepositoryImpl() {
        super(Author.class);
    }

    @Override
    public List<AuthorResponseDto> findAuthorsByName(String name) {
        QAuthor qAuthor = QAuthor.author;
        return from(qAuthor)
                .where(qAuthor.authorName.contains(name))
                .select(Projections.fields(AuthorResponseDto.class,
                        qAuthor.authorId,
                        qAuthor.authorName))
                .fetch();
    }


}
