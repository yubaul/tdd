package io.hhplus.tdd.domain;

import io.hhplus.tdd.database.PointHistory;
import io.hhplus.tdd.database.TransactionType;
import io.hhplus.tdd.database.UserPoint;

public interface PointStore {

    UserPoint store(long id, long amount);

    PointHistory store(long id, long amount, TransactionType type, long updateMillis);

}
