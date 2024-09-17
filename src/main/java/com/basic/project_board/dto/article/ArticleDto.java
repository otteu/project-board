package com.basic.project_board.dto.article;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {

    private long id;
    private String title;
    private String content;

}
