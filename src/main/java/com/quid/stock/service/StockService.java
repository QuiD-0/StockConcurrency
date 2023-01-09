package com.quid.stock.service;

import com.quid.stock.repository.StockJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockJpaRepository stockJpaRepository;

    @Transactional
    public void decreaseStock(Long productId, Long quantity) {
        stockJpaRepository.findByProductId(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"))
            .decreaseStock(quantity);
    }

}
