package com.quid.stock.service;

import com.quid.stock.repository.StockRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockRedisFacade {

        private final StockRedisRepository stockRedisRepository;
        private final StockService stockService;

        public void lockAndDecreaseStock(long productId, long quantity)
            throws InterruptedException {
                while (!stockRedisRepository.lock(productId)) {
                    Thread.sleep(100);
                }
                try {
                    stockService.decreaseStock(productId, quantity);
                } finally {
                    stockRedisRepository.unlock(productId);
                }
        }

}
