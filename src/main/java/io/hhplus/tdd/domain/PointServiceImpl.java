package io.hhplus.tdd.domain;

import io.hhplus.tdd.database.PointHistory;
import io.hhplus.tdd.database.TransactionType;
import io.hhplus.tdd.database.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService{

    private final PointReader pointReader;
    private final PointStore pointStore;
    private final PointPolicy pointPolicy;


    @Override
    public UserPoint getPoint(long id) {
        return pointReader.getPoint(id).toUserPoint();
    }

    @Override
    public List<PointHistory> getHistories(long id) {
        return pointReader.getHistories(id);
    }

    @Override
    public UserPoint charge(long id, long amount) {
        Point point = pointReader.getPoint(id);
        pointPolicy.validateCharge(point.getPoint(), amount);
        point.charge(amount);

        pointStore.store(point.getId(), point.getPoint());
        pointStore.store(point.getId(), amount, TransactionType.CHARGE, System.currentTimeMillis());
        return pointReader.getPoint(id).toUserPoint();
    }

    @Override
    public UserPoint use(long id, long amount) {
        Point point = pointReader.getPoint(id);
        pointPolicy.validateUse(point.getPoint(), amount);
        point.use(amount);

        pointStore.store(id, point.getPoint());
        pointStore.store(id, amount, TransactionType.USE, System.currentTimeMillis());
        return pointReader.getPoint(id).toUserPoint();
    }
}
