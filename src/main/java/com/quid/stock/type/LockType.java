package com.quid.stock.type;

public enum LockType {
    NONE,
    PESSIMISTIC_WRITE,
    OPTIMISTIC,
    NAMED_LOCK,
    REDIS_LETTUCE,
    REDIS_REDISSON,
}
