package com.hs_esslingen.insy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.dto.ArticleDTO;
import com.hs_esslingen.insy.exception.BadRequestException;
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
            throw new BadRequestException("Article with id " + id + " not found.");
        }

        Article article = optionalArticle.get();

        // Patch-Felder aktualisieren
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

        // Nach dem Speichern prüfen, ob alle Artikel in der Bestellung inventarisiert
        // sind
        // und die Bestellung als bearbeitet markieren, wenn ja.
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

    public ResponseEntity<ArticleDTO> createArticle(Integer orderId, ArticleDTO articleDTO) {
        Order order = orderService.retrieveOrderById(orderId)
                .orElseThrow(() -> new BadRequestException("Order with id " + orderId + " not found."));

        Article article = articleMapper.toEntity(articleDTO);
        article.setOrder(order);
        Article savedArticle = articleRepository.save(article);

        return ResponseEntity.ok(articleMapper.toDto(savedArticle));
    }

    public ResponseEntity<ArticleDTO> getArticleById(Integer articleId) {
        Optional<Article> article = articleRepository.findById(articleId);
        if (article.isPresent()) {
            ArticleDTO dto = articleMapper.toDto(article.get());
            return ResponseEntity.ok(dto);
        } else {
            throw new BadRequestException("Article with id " + articleId + " not found.");
        }
    }

    public ResponseEntity<Void> deleteArticle(Integer articleId) {
        Optional<Article> article = articleRepository.findById(articleId);
        if (article.isPresent()) {
            articleRepository.delete(article.get());
            return ResponseEntity.noContent().build();
        } else {
            throw new BadRequestException("Article with id " + articleId + " not found.");
        }
    }
}
