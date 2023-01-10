package com.quid.stock.service;

import com.quid.stock.repository.StockJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NamedLockFacade {

    private final StockJpaRepository stockJpaRepository;

    private final StockService stockService;

    @Transactional
    public void decrease(Long productId, Long quantity) {
        try {
            stockJpaRepository.getLock(productId.toString());
            stockService.decreaseStock(productId, quantity);
        } finally {
            stockJpaRepository.releaseLock(productId.toString());
        }
    }
}
