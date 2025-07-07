package point;

import io.hhplus.tdd.domain.*;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PointServiceUnitTest {

    @InjectMocks
    PointServiceImpl pointService;

    @Mock
    PointReader pointReader;

    @Mock
    PointStore pointStore;

    @Mock
    PointPolicy pointPolicy;


    @Test
    void 특정유저_조회하기(){
        // given
        long userId = 1;
        Point point = Point.builder()
            .id(1)
            .build();
        when(pointReader.getPoint(userId)).thenReturn(point);

        // when
        UserPoint result = pointService.getPoint(userId);

        // then
        Assertions.assertThat(result.id()).isEqualTo(userId);
        verify(pointReader, times(1)).getPoint(userId);
    }

    @Test
    void 충전이용내역_조회하기(){
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
        when(pointReader.getHistories(userId)).thenReturn(pointHistories);

        // when
        List<PointHistory> result = pointService.getHistories(userId);

        // then
        Assertions.assertThat(result.get(0).amount()).isEqualTo(amount);
        verify(pointReader, times(1)).getHistories(userId);
    }

    @Test
    void 특정유저_포인트_충전하기(){
        // given
        // 봉유 포인트 0원에서 5,000원 충전
        long userId = 1;
        long chargeAmount = 5000;
        long initPoint = 0;
        Point afterPoint = Point.builder()
            .id(userId)
            .point(initPoint)
            .build();
        Point beforePoint = Point.builder()
            .id(userId)
            .point(chargeAmount)
            .build();

        when(pointReader.getPoint(userId))
                .thenReturn(afterPoint)
                .thenReturn(beforePoint);
        // when
        UserPoint result = pointService.charge(userId, chargeAmount);

        // then
        // Point.charge() 메소드에서 포인트 증가가 된지 검증
        // 호출 했을 시 실제 기대했던 매개변수와 전달된 매개변수가 일치하는지 검증
        Assertions.assertThat(result.point()).isEqualTo(chargeAmount);
        verify(pointPolicy, times(1)).validateCharge(chargeAmount);
        verify(pointReader, times(2)).getPoint(userId);
        verify(pointStore, times(1)).store(userId, chargeAmount);
        verify(pointStore, times(1)).store(eq(userId), eq(chargeAmount), eq(TransactionType.CHARGE), any(Long.class));
    }


    @Test
    void 특정유저_포인트_사용하기(){
        // given
        // 보유 포인트 50,000원 중 5,000원 사용
        long userId = 1;
        long point = 50000;
        long useAmount = 5000;
        long resultAmount = point - useAmount;
        Point afterPoint = Point.builder()
                .id(userId)
                .point(point)
                .build();
        Point beforePoint = Point.builder()
                .id(userId)
                .point(resultAmount)
                .build();

        when(pointReader.getPoint(userId))
                .thenReturn(afterPoint)
                .thenReturn(beforePoint);

        // when
        UserPoint result = pointService.use(userId, useAmount);

        // then
        // Point.usr() 메소드에서 포인트 차감이 된지 검증
        // 호출 했을 시 실제 기대했던 매개변수와 전달된 매개변수가 일치하는지 검증
        Assertions.assertThat(result.point()).isEqualTo(resultAmount);
        verify(pointPolicy, times(1)).validateUse(point, useAmount);
        verify(pointReader, times(2)).getPoint(userId);
        verify(pointStore, times(1)).store(userId, resultAmount);
        verify(pointStore, times(1)).store(eq(userId), eq(useAmount), eq(TransactionType.USE), any(Long.class));

    }


}
