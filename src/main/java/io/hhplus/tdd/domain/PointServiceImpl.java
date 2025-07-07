package io.hhplus.tdd.domain;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService{
    @Override
    public UserPoint getPoint(long id) {
        return null;
    }

    @Override
    public List<PointHistory> getHistories(long id) {
        return null;
    }

    @Override
    public UserPoint charge(long id, long amount) {
        return null;
    }

    @Override
    public UserPoint use(long id, long amount) {
        return null;
    }
}
