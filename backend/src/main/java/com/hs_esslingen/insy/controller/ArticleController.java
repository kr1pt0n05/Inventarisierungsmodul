package com.hs_esslingen.insy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hs_esslingen.insy.dto.ArticleDTO;
import com.hs_esslingen.insy.service.ArticleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders/{orderId}/items")
public class ArticleController {

    private final ArticleService articleService;

    /**
     * Retrieves all articles in an order.
     *
     * @return a ResponseEntity containing a list of ArticleDTOs
     */
    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getAllArticles(@PathVariable(name = "orderId") Integer orderId) {
        return articleService.getAllArticles(orderId);
    }

    /**
     * Creates a new article in an order.
     *
     * @param orderId    the ID of the order to which the article belongs
     * @param articleDTO the data transfer object containing the article
     *                   information
     * @return a ResponseEntity containing the created ArticleDTO
     */
    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(@PathVariable(name = "orderId") Integer orderId,
            @RequestBody ArticleDTO articleDTO) {
        return articleService.createArticle(orderId, articleDTO);
    }

    /**
     * Updates an article in an order.
     *
     * @param id  the ID of the article to update
     * @param dto the data transfer object containing the updated article
     *            information
     * @return a ResponseEntity containing the updated ArticleDTO
     */
    @PatchMapping("/{itemId}")
    public ResponseEntity<ArticleDTO> updateArticle(
            @PathVariable("itemId") Integer articleId,
            @RequestBody ArticleDTO dto) {
        return articleService.updateArticle(articleId, dto);
    }

    /**
     * Retrieves an article by its ID.
     *
     * @param id the ID of the article to retrieve
     * @return a ResponseEntity containing the ArticleDTO if found, or a 404
     *         Not Found status if not found
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable("itemId") Integer articleId) {
        return articleService.getArticleById(articleId);
    }

    /**
     * Deletes an article by its ID.
     *
     * @param articleId the ID of the article to delete
     * @return a ResponseEntity indicating the result of the deletion
     */
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("itemId") Integer articleId) {
        return articleService.deleteArticle(articleId);
    }
}
