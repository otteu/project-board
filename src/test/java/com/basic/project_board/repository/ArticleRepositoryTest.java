package com.basic.project_board.repository;

import com.basic.project_board.config.JpaConfig;
import com.basic.project_board.domain.Article;
import com.basic.project_board.domain.QArticle;
import com.basic.project_board.dto.article.ArticleDto;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.basic.project_board.domain.QArticle.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@Import(JpaConfig.class)
@Rollback
class ArticleRepositoryTest {

    private ArticleRepository articleRepository;
    private EntityManager entityManager;

    public ArticleRepositoryTest(
            @Autowired ArticleRepository articleRepository,
            @Autowired EntityManager entityManager
    ) {
        this.articleRepository = articleRepository;
        this.entityManager = entityManager;
    }

    @BeforeEach
    public void before() {
        Article article0 = Article.of("new article1", "new content", "#spring");
        Article article1 = Article.of("new article", "no content", "#spring");
        Article article2 = Article.of("new article", "new content", "#spring");
        Article article3 = Article.of("new article", "new content", "#spring");
        Article article4 = Article.of("new article", "no content", "#spring");

        articleRepository.saveAll(List.of(article0, article1, article2, article3, article4));
    }


    @DisplayName("paging을 할 수 있다.")
    @Test
    void paging_test() {





        PageRequest pageRequest = PageRequest.of(0,5, Sort.Direction.DESC, "content");

        Page<Article> page =articleRepository.findByContent("new content", pageRequest);

        // then
        List<Article> content = page.getContent();
        long totalElements = page.getTotalElements();

        Page<ArticleDto> toMap = page.map(article -> new ArticleDto(article.getId(), article.getTitle(), article.getContent()));

        for(ArticleDto articleDto : toMap) {
            System.out.println("ArticleDto : " + articleDto);
        }


        for(Article article : content) {
            System.out.println("article : " + article);
        }
        System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(3);
//        assertThat(page.isFirst()).isTrue();
//        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void startQuerydsl() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);


        Article findArticle = queryFactory
                .select(article)
                .from(article)
                .where(article.title.eq("new article1"))
                .fetchOne();

        assertThat(findArticle.getTitle()).isEqualTo("new article1");

        QueryResults<Article> results = queryFactory
                .selectFrom(article)
                .fetchResults();

        List<Article> content = results.getResults();
        results.getTotal();

    }

}