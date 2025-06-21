package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.dto.ArticleDTO;
import com.hs_esslingen.insy.exception.NotFoundException;
import com.hs_esslingen.insy.mapper.ArticleMapper;
import com.hs_esslingen.insy.model.Article;
import com.hs_esslingen.insy.model.Order;
import com.hs_esslingen.insy.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final OrderService orderService;

    /**
     * Updates an article with the given ID using the provided ArticleDTO.
     * Only fields that are not null in the DTO will be updated.
     * If isInventoried is set to true, it checks if all articles in the
     * associated order are inventoried and marks the order as processed if they
     * are.
     *
     * @param id  the ID of the article to update
     * @param dto the ArticleDTO containing the updated information
     * @return a ResponseEntity containing the updated ArticleDTO
     */
    public ResponseEntity<ArticleDTO> updateArticle(Integer id, ArticleDTO dto) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isEmpty()) {
            throw new NotFoundException("Article with id " + id + " not found.");
        }

        Article article = optionalArticle.get();

        // Update patch fields
        if (dto.getInventoriesId() != null) {
            article.setInventoriesId(dto.getInventoriesId());
        }
        if (dto.getDescription() != null) {
            article.setDescription(dto.getDescription());
        }
        if (dto.getPrice() != null) {
            article.setPrice(dto.getPrice());
        }
        if (dto.getCompany() != null) {
            article.setCompany(dto.getCompany());
        }
        if (dto.getIsInventoried() != null) {
            article.setIsInventoried(dto.getIsInventoried());
        }

        if (dto.getSerialNumber() != null) {
            article.setSerialNumber(dto.getSerialNumber());
        }
        if (dto.getLocation() != null) {
            article.setLocation(dto.getLocation());
        }
        if (dto.getOrderer() != null) {
            article.setUser(dto.getOrderer());
        }
        if (dto.getIsExtension() != null) {
            article.setIsExtension(dto.getIsExtension());
        }

        if (dto.getTags() != null) {
            article.setTags(dto.getTags());
        }

        Article saved = articleRepository.save(article);

        // After saving, check if all items in the order have been inventoried
        // and mark the order as processed if they have.
        if (dto.getIsInventoried()) {
            Order order = saved.getOrder();
            boolean allInventoried = order.getArticles().stream()
                    .allMatch(art -> art.getIsInventoried() != null && art.getIsInventoried());

            if (allInventoried) {
                orderService.markOrderAsProcessed(order.getId());
            }
        }

        return ResponseEntity.ok(articleMapper.toDto(saved));
    }

    /**
     * Retrieves all articles associated with a specific order.
     *
     * @param orderId the ID of the order for which to retrieve articles
     * @return a ResponseEntity containing a list of ArticleDTOs or no content if
     *         none found
     */
    public ResponseEntity<List<ArticleDTO>> getAllArticles(Integer orderId) {
        List<Article> articles = orderService.getArticlesByOrder(orderId);
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<ArticleDTO> articleDTOs = articles.stream()
                .map(articleMapper::toDto)
                .toList();
        return ResponseEntity.ok(articleDTOs);
    }

    /**
     * Creates a new article associated with a specific order.
     *
     * @param orderId    the ID of the order to associate with the article
     * @param articleDTO the ArticleDTO containing the article information
     * @return a ResponseEntity containing the created ArticleDTO
     */
    public ResponseEntity<ArticleDTO> createArticle(Integer orderId, ArticleDTO articleDTO) {
        Order order = orderService.retrieveOrderById(orderId)
                .orElseThrow(() -> new NotFoundException("Order with id " + orderId + " not found."));

        Article article = articleMapper.toEntity(articleDTO);
        article.setOrder(order);
        Article savedArticle = articleRepository.save(article);

        return ResponseEntity.ok(articleMapper.toDto(savedArticle));
    }

    /**
     * Retrieves an article by its ID.
     *
     * @param articleId the ID of the article to retrieve
     * @return a ResponseEntity containing the ArticleDTO if found
     * @throws NotFoundException if the article with the given ID does not exist
     */
    public ResponseEntity<ArticleDTO> getArticleById(Integer articleId) {
        Optional<Article> article = articleRepository.findById(articleId);
        if (article.isPresent()) {
            ArticleDTO dto = articleMapper.toDto(article.get());
            return ResponseEntity.ok(dto);
        } else {
            throw new NotFoundException("Article with id " + articleId + " not found.");
        }
    }

    /**
     * Deletes an article by its ID.
     *
     * @param articleId the ID of the article to delete
     * @return a ResponseEntity with no content if the deletion was successful
     * @throws NotFoundException if the article with the given ID does not exist
     */
    public ResponseEntity<Void> deleteArticle(Integer articleId) {
        Optional<Article> article = articleRepository.findById(articleId);
        if (article.isPresent()) {
            articleRepository.delete(article.get());
            return ResponseEntity.noContent().build();
        } else {
            throw new NotFoundException("Article with id " + articleId + " not found.");
        }
    }
}
