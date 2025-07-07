package io.hhplus.tdd.domain;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.UserPoint;

import java.util.List;

public interface PointService {

    UserPoint getPoint(long id);

    List<PointHistory> getHistories(long id);

    UserPoint charge(long id, long amount);

    UserPoint use(long id, long amount);

}
