package com.quid.stock.service;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.quid.stock.domain.Stock;
import com.quid.stock.repository.StockJpaRepository;
import com.quid.stock.type.LockType;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StockRedisFacadeTest {
    @Autowired
    private StockRedisFacade stockRedisFacade;
    @Autowired
    private StockJpaRepository stockJpaRepository;

    @BeforeEach
    void setUp() {
        Stock stock = Stock.builder()
            .productId(1L)
            .quantity(10L)
            .build();
        stockJpaRepository.saveAndFlush(stock);
    }

    @AfterEach
    void tearDown() {
        stockJpaRepository.deleteAll();
    }

    @Test
    void raceConditionWithRedis() throws InterruptedException {
        runDecreaseThread();

        stockJpaRepository.findByProductId(1L)
            .ifPresent(stock -> assertEquals(0L, stock.getQuantity()));
    }


    private void runDecreaseThread() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executorService = newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockRedisFacade.lockAndDecreaseStock(1L, 1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
    }

}