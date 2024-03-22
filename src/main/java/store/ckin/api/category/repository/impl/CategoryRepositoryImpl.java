package store.ckin.api.category.repository.impl;

import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.ckin.api.book.relationship.bookcategory.entity.QBookCategory;
import store.ckin.api.category.entity.Category;
import store.ckin.api.category.repository.CategoryRepositoryCustom;

/**
 * CategoryRepositoryImpl
 *
 * @author : 이가은
 * @version : 2024. 03. 16
 */
public class CategoryRepositoryImpl extends QuerydslRepositorySupport implements CategoryRepositoryCustom {
    public CategoryRepositoryImpl() {
        super(Category.class);
    }

    QBookCategory bookCategory = QBookCategory.bookCategory;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Long> getParentIds(List<Long> bookIds) {
        return from(bookCategory)
                .select(bookCategory.category.categoryId)
                .where(bookCategory.book.bookId.in(bookIds))
                .distinct()
                .fetch();
    }
}
