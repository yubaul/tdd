package point;

import io.hhplus.tdd.domain.PointService;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PointServiceUnitTest {

    @Mock
    PointService pointService;


    @Test
    void 특정_유저_조회하기(){
        // given
        long userId = 1;
        UserPoint userPoint = UserPoint.empty(userId);
        Mockito.when(pointService.getPoint(userId)).thenReturn(userPoint);

        // when
        UserPoint result = pointService.getPoint(userId);

        // then
        Assertions.assertThat(result.id()).isEqualTo(userId);
        Mockito.verify(pointService, Mockito.times(1)).getPoint(userId);
    }

    @Test
    void 충전_이용_내역을_조회하기(){
        // given
        long userId = 1;
        long amount = 5000;
        List<PointHistory> pointHistories = new ArrayList<>();
        PointHistory pointHistory = new PointHistory(
                1,
                userId,
                amount,
                TransactionType.CHARGE,
                System.currentTimeMillis()
        );
        pointHistories.add(pointHistory);
        Mockito.when(pointService.getHistories(userId)).thenReturn(pointHistories);

        // when
        List<PointHistory> result = pointService.getHistories(userId);

        // then
        Assertions.assertThat(result.get(0).amount()).isEqualTo(amount);
        Mockito.verify(pointService, Mockito.times(1)).getHistories(userId);
    }

    @Test
    void 충전하기(){
        // given
        long userId = 1;
        long amount = 5000;
        long updateMillis = System.currentTimeMillis();
        UserPoint userPoint = new UserPoint(userId, amount, updateMillis);
        Mockito.when(pointService.charge(userId,amount)).thenReturn(userPoint);

        // when
        UserPoint result = pointService.charge(userId, amount);

        // then
        Assertions.assertThat(result.point()).isEqualTo(amount);
        Mockito.verify(pointService, Mockito.times(1)).charge(userId,amount);
    }

    @Test
    void 특정유저_포인트_사용하기(){
        // given
        long userId = 1;
        long amount = 0;
        long useAmount = 5000;
        long updateMillis = System.currentTimeMillis();
        UserPoint userPoint = new UserPoint(userId, amount, updateMillis);
        Mockito.when(pointService.use(userId, useAmount)).thenReturn(userPoint);

        // when
        UserPoint result = pointService.use(userId, useAmount);

        // then
        Assertions.assertThat(result.point()).isEqualTo(amount);
        Mockito.verify(pointService, Mockito.times(1)).use(userId, useAmount);

    }
}
