package io.hhplus.tdd.domain;

import io.hhplus.tdd.database.PointHistory;


import java.util.List;

public interface PointReader {

    Point getPoint(long id);

    List<PointHistory> getHistories(long id);

}
