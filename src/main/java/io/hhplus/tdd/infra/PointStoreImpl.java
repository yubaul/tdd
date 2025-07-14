package io.hhplus.tdd.infra;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.domain.PointStore;
import io.hhplus.tdd.database.PointHistory;
import io.hhplus.tdd.database.TransactionType;
import io.hhplus.tdd.database.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointStoreImpl implements PointStore {

    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;


    @Override
    public UserPoint store(long id, long amount) {
        return userPointTable.insertOrUpdate(id, amount);
    }

    @Override
    public PointHistory store(long id, long amount, TransactionType type, long updateMillis) {
        return pointHistoryTable.insert(id, amount, type,updateMillis);
    }
}
