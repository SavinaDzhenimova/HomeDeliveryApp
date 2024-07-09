package com.homedelivery.service.scheduler;

import com.homedelivery.model.entity.BaseEntity;
import com.homedelivery.model.entity.Comment;
import com.homedelivery.model.entity.Order;
import com.homedelivery.repository.CommentRepository;
import com.homedelivery.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CronScheduler {

    private final Logger LOGGER = LoggerFactory.getLogger(CronScheduler.class);
    private final OrderRepository orderRepository;
    private final CommentRepository commentRepository;

    public CronScheduler(OrderRepository orderRepository, CommentRepository commentRepository) {
        this.orderRepository = orderRepository;
        this.commentRepository = commentRepository;
    }

    @Scheduled(cron = "0 0 3 1/1 * *")
    public void deleteOrdersDeliveredOnBeforeMoreThanSixMonths() {

        List<Order> orders = this.orderRepository.findAll();

        LocalDateTime scheduledDate = LocalDateTime.now().minusMonths(6L);

        List<Long> ids = orders.stream()
                .filter(order -> {

                    LocalDateTime deliveredOn = order.getDeliveredOn();

                    if (deliveredOn != null) {
                        return deliveredOn.isBefore(scheduledDate);
                    }

                    return false;
                })
                .map(BaseEntity::getId).toList();

        this.orderRepository.deleteAllById(ids);

        LOGGER.info("Deleted orders delivered on before more than 6 months!");
    }

    @Scheduled(cron = "0 0 2 1/1 * *")
    public void deleteCommentsAddedOnBeforeMoreThanOneYear() {

        List<Comment> comments = this.commentRepository.findAll();

        LocalDateTime scheduledDate = LocalDateTime.now().minusYears(1L);

        List<Long> ids = comments.stream()
                .filter(order -> {

                    LocalDateTime addedOn = order.getAddedOn();

                    if (addedOn != null) {
                        return addedOn.isBefore(scheduledDate);
                    }

                    return false;
                })
                .map(BaseEntity::getId).toList();

        this.commentRepository.deleteAllById(ids);

        LOGGER.info("Deleted comments added on before more than 1 year!");
    }

}
