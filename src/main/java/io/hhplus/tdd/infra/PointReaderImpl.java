package io.hhplus.tdd.infra;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.domain.Point;
import io.hhplus.tdd.domain.PointReader;
import io.hhplus.tdd.point.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PointReaderImpl implements PointReader {

    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    @Override
    public Point getPoint(long id) {
        return Point.toPoint(userPointTable.selectById(id)) ;
    }

    @Override
    public List<PointHistory> getHistories(long id) {
        return pointHistoryTable.selectAllByUserId(id);
    }
}
