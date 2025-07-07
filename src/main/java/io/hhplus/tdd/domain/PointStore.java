package io.hhplus.tdd.domain;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;

public interface PointStore {

    UserPoint store(long id, long amount);

    PointHistory store(long id, long amount, TransactionType type, long updateMillis);

}
