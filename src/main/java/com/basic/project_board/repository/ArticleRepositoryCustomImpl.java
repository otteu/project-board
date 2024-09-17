package com.basic.project_board.repository;

import com.basic.project_board.domain.Article;
import com.basic.project_board.domain.QArticle;
import com.basic.project_board.dto.article.ArticleDto;
import com.basic.project_board.dto.article.ArticleSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.basic.project_board.domain.QArticle.article;
import static org.springframework.util.StringUtils.hasText;


public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ArticleRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Article> search(ArticleSearchCondition condition) {
        return queryFactory
                .select(new ArticleDto(article.getId(), article.getTitle(), article.getContent()))
                .from(article)
                .leftJoin(article.articleComments)
                .where(
                    titleEq(condition.getTitle()),
                    contentEq(condition.getContent())
                )
                .fetch();
    }

    private BooleanExpression titleEq(String title) {
        return hasText(title) ? article.title.eq(title) : null;
    }

    private BooleanExpression contentEq(String content) {
        return hasText(content) ? article.content.eq(content) : null;
    }


}
