package com.quid.stock.service;

import com.quid.stock.repository.StockRedisRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockRedisFacade {

        private final StockRedisRepository stockRedisRepository;
        private final RedissonClient redisonClient;
        private final StockService stockService;

        public void decreaseStockWithRedisLettuce(long productId, long quantity)
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

        public void decreaseStockWithRedisRadisson(Long productId, long quantity) {
            RLock lock = redisonClient.getLock("productId::"+productId.toString());
            try {
                if(!lock.tryLock(5,1, TimeUnit.SECONDS)) {
                    throw new RuntimeException("Lock is not available");
                }
                stockService.decreaseStock(productId, quantity);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }

}
