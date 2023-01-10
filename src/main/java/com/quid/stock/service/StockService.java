package com.quid.stock.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface StockService {
    void decreaseStock(Long productId, Long quantity);
    void decreaseStockWithPessimisticLock(Long productId, Long quantity);
    void decreaseStockWithOptimisticLock(Long productId, Long quantity);
}
