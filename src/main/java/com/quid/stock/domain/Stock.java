package com.quid.stock.domain;


import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long productId;

    private Long quantity;

    //optimistic lock을 위한 version
    @Version
    private Long version;

    @Builder
    public Stock(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public void decreaseStock(Long quantity) {
        if(this.quantity < quantity) {
            throw new RuntimeException("Not enough stock");
        }
        this.quantity -= quantity;
    }
}
