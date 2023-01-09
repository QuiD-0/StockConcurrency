package com.quid.stock.service;

import org.springframework.stereotype.Service;

@Service
public interface StockService {
    void decreaseStock(Long productId, Long quantity);
    void decreaseStockWithPessimisticLock(Long productId, Long quantity);

}
