package store.ckin.api.author.repository.impl;

import com.querydsl.core.types.Projections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.ckin.api.author.dto.response.AuthorResponseDto;
import store.ckin.api.author.entity.Author;
import store.ckin.api.author.entity.QAuthor;
import store.ckin.api.author.repository.AuthorRepositoryCustom;

import java.util.List;

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

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<AuthorResponseDto> findAuthorsByName(String name, Pageable pageable) {
        QAuthor author = QAuthor.author;
        List<AuthorResponseDto> authors = from(author)
                .where(author.authorName.contains(name))
                .orderBy(author.authorName.asc())
                .select(Projections.fields(AuthorResponseDto.class,
                        author.authorId,
                        author.authorName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 결과 수를 얻기 위한 쿼리
        long total = from(author)
                .where(author.authorName.contains(name))
                .fetchCount();

        return new PageImpl<>(authors, pageable, total);
    }


}
