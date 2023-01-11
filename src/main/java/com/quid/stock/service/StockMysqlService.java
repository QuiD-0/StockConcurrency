package com.quid.stock.service;

import com.quid.stock.domain.Stock;
import com.quid.stock.repository.StockJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockMysqlService {

    private final StockJpaRepository stockJpaRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decreaseStock(Long productId, Long quantity) {
        Stock stock = stockJpaRepository.findByProductId(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        stock.decreaseStock(quantity);
        stockJpaRepository.saveAndFlush(stock);
    }

    @Transactional
    public void decreaseStockWithPessimisticLock(Long productId, Long quantity) {
        Stock stock = stockJpaRepository.findByProductIdWithPessimisticLock(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        stock.decreaseStock(quantity);
        stockJpaRepository.saveAndFlush(stock);
    }

    @Transactional
    public void decreaseStockWithOptimisticLock(Long productId, Long quantity) {
        Stock stock = stockJpaRepository.findByProductIdWithOptimisticLock(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        stock.decreaseStock(quantity);
        stockJpaRepository.saveAndFlush(stock);
    }

}

