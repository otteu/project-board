package com.basic.project_board.repository;

import com.basic.project_board.domain.ArticleComment;
import com.basic.project_board.domain.QArticle;
import com.basic.project_board.domain.QArticleComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>,
        QuerydslBinderCustomizer<QArticleComment>
{
    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root) {

        bindings.excludeUnlistedProperties(true);
        bindings.including(root.content, root.createAt, root.createdBy);
        // containsIgnoreCase: 대소문자 구별 X
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);

    }
}
