package com.quid.stock.service;

import com.quid.stock.repository.StockJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockJpaRepository stockJpaRepository;

    @Override
    @Transactional
    public void decreaseStock(Long productId, Long quantity) {
        stockJpaRepository.findByProductId(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"))
            .decreaseStock(quantity);
    }

    @Override
    @Transactional
    public void decreaseStockWithPessimisticLock(Long productId, Long quantity) {
        stockJpaRepository.findByProductIdWithPessimisticLock(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"))
            .decreaseStock(quantity);
    }

}

