package com.basic.project_board.repository;

import com.basic.project_board.config.JpaConfig;
import com.basic.project_board.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


// @ActiveProfiles("testdb")
@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    private ArticleRepository articleRepository;
    private ArticleCommentRepository articleCommentRepository;

    public JpaRepositoryTest(
            @Autowired
            ArticleRepository articleRepository,
            @Autowired
            ArticleCommentRepository articleCommentRepository
    ) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("여러개의 게시글을 조회 합니다.")
    @Test
    void article_select() {

        // given

        // when
        List<Article> articles = articleRepository.findAll();

        // then
        assertThat(articles)
                .isNotNull()
                .hasSize(0);
    }

    @DisplayName("게시글을 작성 합니다.")
    @Test
    void write_articles() {

        // given
        long previousCount = articleRepository.count();
        Article article = Article.of("new article", "new content", "#spring");
        articleRepository.save(article);

        // List<Article> articles = articleRepository.findAll();\


        assertThat(articleRepository.count())
                .isEqualTo(previousCount + 1);
    }

    @DisplayName("게시글을 수정 합니다.")
    @Test
    void update_article() {
        String updatedHashtag = "#springbood";
        Article article = Article.of("new article", "new content", "#spring");

        Article findArticle = articleRepository.save(article);
        findArticle.setHashtag(updatedHashtag);

        articleRepository.saveAndFlush(findArticle);

        Article findOneArticle = articleRepository.findById(findArticle.getId()).orElseThrow();

        assertThat(findOneArticle)
                .hasFieldOrPropertyWithValue("hashtag", updatedHashtag);

    }
    @DisplayName("게시물을 삭제 합니다.")
    @Test
    void delete_article() {
        Article article = Article.of("new article", "new content", "#spring");

        Article findArticle = articleRepository.save(article);
        Long saveCount = articleRepository.count();

        articleRepository.save(findArticle);

        articleRepository.delete(findArticle);

        assertThat(articleRepository.count())
                .isEqualTo(saveCount - 1);
    }


}