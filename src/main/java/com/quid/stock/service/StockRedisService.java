package com.quid.stock.service;

import com.quid.stock.repository.StockRedisRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockRedisService {

        private final StockRedisRepository stockRedisRepository;
        private final RedissonClient redisonClient;
        private final StockMysqlService stockMysqlService;

        public void decreaseStockWithRedisLettuce(long productId, long quantity)
            throws InterruptedException {
                while (!stockRedisRepository.lock(productId)) {
                    Thread.sleep(100);
                }
                try {
                    stockMysqlService.decreaseStock(productId, quantity);
                } finally {
                    stockRedisRepository.unlock(productId);
                }
        }

        public void decreaseStockWithRedisRadisson(Long productId, long quantity)
            throws InterruptedException {
            RLock lock = redisonClient.getLock("productId::"+productId.toString());
            try {
                if(lock.tryLock(5,1, TimeUnit.SECONDS)) {
                    stockMysqlService.decreaseStock(productId, quantity);
                }
            }finally {
                lock.unlock();
            }
        }

}
