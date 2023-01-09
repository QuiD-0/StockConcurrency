package com.quid.stock.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.quid.stock.domain.Stock;
import com.quid.stock.repository.StockJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockJpaRepository stockJpaRepository;

    @BeforeEach
    void setUp() {
        Stock stock = Stock.builder()
            .productId(1L)
            .quantity(10L)
            .build();
        stockJpaRepository.save(stock);
    }

    @AfterEach
    void tearDown() {
        stockJpaRepository.deleteAll();
    }

    @Test
    void decreaseStock() {
        stockService.decreaseStock(1L, 1L);
        stockJpaRepository.findByProductId(1L).ifPresent(stock -> assertEquals(9L, stock.getQuantity()));
    }

}