package point.unit;

import io.hhplus.tdd.domain.*;
import io.hhplus.tdd.database.PointHistory;
import io.hhplus.tdd.database.TransactionType;
import io.hhplus.tdd.database.UserPoint;
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


    @Test
    void 특정유저_보유_포인트_조회하기(){
        // given
        long userId = 1L;
        Point point = Point.builder()
            .id(userId)
            .point(0L)
            .updateMillis(System.currentTimeMillis())
            .build();
        when(pointReader.getPoint(userId)).thenReturn(point);

        // when
        UserPoint result = pointService.getPoint(userId);

        // then
        Assertions.assertThat(result.id()).isEqualTo(userId);
        verify(pointReader, times(1)).getPoint(userId);
    }

    @Test
    void 특정유저_충전이용내역_조회하기(){
        // given
        long userId = 1L;
        long amount = 5_000L;
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
        long userId = 1L;
        long amountToCharge = 5_000L;
        long initPoint = 0L;
        Point afterPoint = Point.builder()
            .id(userId)
            .point(initPoint)
            .updateMillis(System.currentTimeMillis())
            .build();
        Point beforePoint = Point.builder()
            .id(userId)
            .point(amountToCharge)
            .updateMillis(System.currentTimeMillis())
            .build();

        when(pointReader.getPoint(userId))
            .thenReturn(afterPoint)
            .thenReturn(beforePoint);
        // when
        UserPoint result = pointService.charge(userId, amountToCharge);

        // then
        // Point.charge() 메소드에서 포인트 증가가 된지 검증
        // 호출 했을 시 실제 기대했던 매개변수와 전달된 매개변수가 일치하는지 검증
        Assertions.assertThat(result.point()).isEqualTo(amountToCharge);
        verify(pointReader, times(2)).getPoint(userId);
        verify(pointStore, times(1)).store(userId, amountToCharge);
        verify(pointStore, times(1)).store(eq(userId), eq(amountToCharge), eq(TransactionType.CHARGE), any(Long.class));
    }

    @Test
    void 특정유저_충전시_음수금액_예외() {
        // given
        long userId = 1L;
        long amountToCharge = -5_000L;
        long currentPoint = 0L;
        Point point = Point.builder()
            .id(userId)
            .point(currentPoint)
            .updateMillis(System.currentTimeMillis())
            .build();

        when(pointReader.getPoint(userId)).thenReturn(point);


        // when & then
        Assertions.assertThatThrownBy(() -> pointService.charge(userId, amountToCharge))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("충전 포인트는 0보다 커야 합니다.");
    }

    @Test
    void 특정유저_충전시_최대포인트_초과_예외(){
        long userId = 1L;
        long amountToCharge = 60_000L;
        long currentPoint = 50_000;
        Point point = Point.builder()
            .id(userId)
            .point(currentPoint)
            .updateMillis(System.currentTimeMillis())
            .build();

        when(pointReader.getPoint(userId)).thenReturn(point);

        // when & then
        Assertions.assertThatThrownBy(() -> pointService.charge(userId, amountToCharge))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("보유할 수 있는 최대 포인트(100,000원)를 초과할 수 없습니다.");
    }

    @Test
    void 특정유저_포인트_사용하기(){
        // given
        // 보유 포인트 50,000원 중 5,000원 사용
        long userId = 1;
        long point = 50_000L;
        long amountToUse = 5_000L;
        long resultAmount = point - amountToUse; // 사용하고 남은 금액
        Point afterPoint = Point.builder()
            .id(userId)
            .point(point)
            .updateMillis(System.currentTimeMillis())
            .build();
        Point beforePoint = Point.builder()
            .id(userId)
            .point(resultAmount)
            .updateMillis(System.currentTimeMillis())
            .build();

        when(pointReader.getPoint(userId))
                .thenReturn(afterPoint)
                .thenReturn(beforePoint);

        // when
        UserPoint result = pointService.use(userId, amountToUse);

        // then
        // Point.usr() 메소드에서 포인트 차감이 된지 검증
        // 호출 했을 시 실제 기대했던 매개변수와 전달된 매개변수가 일치하는지 검증
        Assertions.assertThat(result.point()).isEqualTo(resultAmount);
        verify(pointReader, times(2)).getPoint(userId);
        verify(pointStore, times(1)).store(userId, resultAmount);
        verify(pointStore, times(1)).store(eq(userId), eq(amountToUse), eq(TransactionType.USE), any(Long.class));

    }

    @Test
    void 특정유저_사용포인트가_0이하면_예외(){
        // given
        long userId = 1;
        long currentPoint = 50_000L;
        long amountToUse = -1L;
        Point point = Point.builder()
                .id(userId)
                .point(currentPoint)
                .updateMillis(System.currentTimeMillis())
                .build();

        when(pointReader.getPoint(userId)).thenReturn(point);

        // when & then
        Assertions.assertThatThrownBy(() -> pointService.use(userId, amountToUse))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("사용 포인트는 0보다 커야 합니다.");

    }

    @Test
    void 특정유저_보유포인트보다_많이_사용하면_예외(){
        // given
        long userId = 1;
        long currentPoint = 50_000L;
        long amountToUse = 60_000L;
        Point point = Point.builder()
                .id(userId)
                .point(currentPoint)
                .updateMillis(System.currentTimeMillis())
                .build();

        when(pointReader.getPoint(userId)).thenReturn(point);

        // when & then
        Assertions.assertThatThrownBy(() -> pointService.use(userId, amountToUse))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("보유 포인트보다 많이 사용할 수 없습니다.");

    }


}
