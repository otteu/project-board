package com.basic.project_board.repository;

import com.basic.project_board.domain.Article;
import com.basic.project_board.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        // 기본 검색 기능
        QuerydslPredicateExecutor<Article>,
        QuerydslBinderCustomizer<QArticle>
{
    @Override
    default void customize(QuerydslBindings bindings, QArticle root){
        // 선택적으로 검색 할수 있게 한다
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.content, root.hashtag, root.createAt, root.createdBy);
        // containsIgnoreCase: 대소문자 구별 X
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }

    // paging Test
    // @Query(value = "select a from Article a left join a.articleComments",
    @Query(
            countQuery = "select count(a.content) from Article"
    )
    Page<Article> findByContent(String content, Pageable pageable);


    // 연관된 데이터 다 끌고옴
    @Query("select a from Article a left join fetch ArticleComment c")
    List<Article> findArticleFetchJoin();

    @EntityGraph(attributePaths = {"articleComments"})
    List<Article> findAll();



}
