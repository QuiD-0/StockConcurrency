package com.quid.stock.service;

import static com.quid.stock.type.LockType.*;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        stockJpaRepository.findByProductId(1L)
            .ifPresent(stock -> assertEquals(9L, stock.getQuantity()));
    }

    @Test
    void decreaseStock_when_quantity_is_bigger() {
        assertThrows(RuntimeException.class, () -> stockService.decreaseStock(1L, 100L));
    }

    @Test
    void RaceCondition() throws InterruptedException {
        runDecreaseThread(NONE);

        //여러 쓰레드에서 값을 동시에 변경할 경우 원하는 값이 나오지 않을 수 있다.
        stockJpaRepository.findByProductId(1L)
            .ifPresent(stock -> assertNotEquals(0L, stock.getQuantity()));
    }

    @Test
    void RaceConditionWithPessimisticLock() throws InterruptedException {
        runDecreaseThread(PESSIMISTIC_WRITE);

        //Pessimistic Lock을 사용하면 원하는 값이 나온다.
        stockJpaRepository.findByProductId(1L)
            .ifPresent(stock -> assertEquals(0L, stock.getQuantity()));
    }

    private void runDecreaseThread(LockType Op) throws InterruptedException {
        int threadCount = 10;
        ExecutorService executorService = newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    switch (Op) {
                        case NONE -> stockService.decreaseStock(1L, 1L);
                        case PESSIMISTIC_WRITE ->
                            stockService.decreaseStockWithPessimisticLock(1L, 1L);
                        default -> throw new RuntimeException("Invalid Op");
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
    }

}