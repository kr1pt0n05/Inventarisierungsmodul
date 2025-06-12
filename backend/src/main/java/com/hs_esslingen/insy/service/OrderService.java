package com.hs_esslingen.insy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hs_esslingen.insy.dto.ItemCreateDTO;
import com.hs_esslingen.insy.dto.OrderCreateDTO;
import com.hs_esslingen.insy.dto.OrderResponseDTO;
import com.hs_esslingen.insy.exception.BadRequest;
import com.hs_esslingen.insy.mapper.OrderMapper;
import com.hs_esslingen.insy.model.Article;
import com.hs_esslingen.insy.model.Order;
import com.hs_esslingen.insy.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    /**
     * Retrieves all open orders from the repository.
     *
     * @return a list of open orders wrapped in a ResponseEntity
     */
    public ResponseEntity<List<OrderResponseDTO>> getAllOpenOrders() {
        List<Order> openOrders = orderRepository.findAllByDeletedAtIsNull();
        List<OrderResponseDTO> dtos = openOrders.stream()
                .map(orderMapper::toDto)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    /**
     * Marks an order as processed by setting its deletedAt timestamp.
     * This method marks the order as deleted without actually removing it from the
     * database.
     * 
     * @param id the ID of the order to delete
     * @return a ResponseEntity indicating the result of the deletion
     */
    public ResponseEntity<Void> markOrderAsProcessed(Integer id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            order.get().setDeletedAt(LocalDateTime.now());
            logger.info("Marking order with id {} as processed.", order.get().getDeletedAt());
            orderRepository.save(order.get());
            return ResponseEntity.noContent().build();
        } else {
            throw new BadRequest("Order with id " + id + " not found.");
        }
    }

    /**
     * Creates a new order based on the provided MockBesyOrderDTO array.
     * This method checks if an order with the same besyId already exists before
     * creating a new one.
     *
     * @param dtos an array of MockBesyOrderDTO objects containing order details
     * @return a ResponseEntity indicating the result of the creation
     */
    public ResponseEntity<Void> createOrder(OrderCreateDTO[] dtos) {

        if (dtos != null) {
            for (OrderCreateDTO dto : dtos) {
                boolean exists = orderRepository.findByBesyId(dto.getOrder_id()).isPresent();
                if (exists)
                    continue;

                Order order = Order.builder()
                        .description("Bestellung " + dto.getOrder_id())
                        .price(dto.getOrder_quote_price())
                        .company(dto.getSupplier_name())
                        .createdAt(dto.getOrder_created_date())
                        .user(dto.getUser_name())
                        .besyId(dto.getOrder_id())
                        .build();

                for (ItemCreateDTO item : dto.getItems()) {
                    Article article = Article.builder()
                            .description(item.getItem_name())
                            .price(item.getItem_price_per_unit())
                            .company(dto.getSupplier_name())
                            .user(dto.getUser_name())
                            .build();
                    order.addArticle(article);
                }

                orderRepository.save(order);
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Retrieves an order by its ID. Used by the getOrderById-method from
     * OrderController.
     *
     * @param id the ID of the order
     * @return the order details wrapped in a ResponseEntity
     */
    public ResponseEntity<OrderResponseDTO> getOrderById(Integer id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            OrderResponseDTO dto = orderMapper.toDto(order.get());
            return ResponseEntity.ok(dto);
        } else {
            throw new BadRequest("Order with id " + id + " not found.");
        }
    }

    /**
     * Retrieves all articles associated with a specific order.
     *
     * @param orderId the ID of the order
     * @return a list of articles associated with the order
     */
    public List<Article> getArticlesByOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BadRequest("Order with id " + orderId + " not found."));
        return order.getArticles();
    }

    /**
     * Retrieves an order by its ID. Used by the ArticleController to fetch
     * articles for a specific order.
     *
     * @param orderId the ID of the order
     * @return an Optional containing the order if found, or empty if not found
     */
    public Optional<Order> retrieveOrderById(Integer orderId) {
        return orderRepository.findById(orderId);
    }

    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<Order> allOrders = orderRepository.findAllOrderedByCreatedAt();
        List<OrderResponseDTO> dtos = allOrders.stream()
                .map(orderMapper::toDto)
                .toList();

        return ResponseEntity.ok(dtos);
    }

}
