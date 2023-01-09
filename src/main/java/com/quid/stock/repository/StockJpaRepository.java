package com.quid.stock.repository;

import com.quid.stock.domain.Stock;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface StockJpaRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByProductId(long productId);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Stock s where s.productId = :productId")
    Optional<Stock> findByProductIdWithPessimisticLock(long productId);
}
