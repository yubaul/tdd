package io.hhplus.tdd.application;

import io.hhplus.tdd.domain.PointService;
import io.hhplus.tdd.database.PointHistory;
import io.hhplus.tdd.database.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointFacade {

    private final PointService pointService;

    public UserPoint getPoint(long id){
        return pointService.getPoint(id);
    }

    public List<PointHistory> getHistories(long id){
        return pointService.getHistories(id);
    }

    public UserPoint charge(long id, long amount){
        return pointService.charge(id, amount);
    }

    public UserPoint use(long id, long amount){
        return pointService.use(id,amount);
    }

}
