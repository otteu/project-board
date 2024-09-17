package com.basic.project_board.repository;

import com.basic.project_board.domain.Article;
import com.basic.project_board.dto.article.ArticleSearchCondition;

import java.util.List;

public interface ArticleRepositoryCustom {
    List<Article> search(ArticleSearchCondition condition);
}
