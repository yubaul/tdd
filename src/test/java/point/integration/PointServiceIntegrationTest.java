package point.integration;

import io.hhplus.tdd.TddApplication;
import io.hhplus.tdd.database.*;
import io.hhplus.tdd.domain.PointService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = TddApplication.class)
public class PointServiceIntegrationTest {

    @Autowired PointService pointService;
    @Autowired UserPointTable userPointTable;
    @Autowired PointHistoryTable pointHistoryTable;


    @Test
    void 특정유저_포인트_조회하기(){
        // given
        long userId = 1L;
        long amount = 0L;
        userPointTable.insertOrUpdate(userId, amount);

        // when
        UserPoint result = pointService.getPoint(userId);

        // then
        Assertions.assertThat(result.point()).isEqualTo(amount);
    }

    @Test
    void 특정유저_포인트_이용내역_조회하기(){
        // given
        long userId = 2L;
        long amount = 5_000L;
        long amountToCharge = 10_000L;
        long amountToUse = 5_000L;
        userPointTable.insertOrUpdate(userId, amount);
        pointHistoryTable.insert(userId, amountToCharge, TransactionType.CHARGE, System.currentTimeMillis());
        pointHistoryTable.insert(userId, amountToUse, TransactionType.USE, System.currentTimeMillis());

        // when
        List<PointHistory> result = pointService.getHistories(userId);

        // then
        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0).amount()).isEqualTo(amountToCharge);
        Assertions.assertThat(result.get(1).amount()).isEqualTo(amountToUse);
    }

    @Test
    void 특정유저_포인트_충전하기_예외_없음(){
        // given
        long userId = 3L;
        long amount = 0L;
        long amountToCharge = 10_000L;
        userPointTable.insertOrUpdate(userId, amount);

        // when
        UserPoint result = pointService.charge(userId, amountToCharge);
        List<PointHistory> pointHistories = pointService.getHistories(userId);

        // then
        Assertions.assertThat(result.point()).isEqualTo(amountToCharge);
        Assertions.assertThat(pointHistories.get(0).amount()).isEqualTo(amountToCharge);
    }

    @Test
    void 특정유저_포인트_충전시_음수금액_예외(){
        // given
        long userId = 4L;
        long amount = 0L;
        long amountToCharge = -10_000L;
        userPointTable.insertOrUpdate(userId, amount);

        // when & then
        Assertions.assertThatThrownBy(() -> pointService.charge(userId, amountToCharge))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("충전 포인트는 0보다 커야 합니다.");

    }

    @Test
    void 특정유저_포인트_충전시_최대포인트_초과_예외(){
        // given
        long userId = 5L;
        long amount = 0L;
        long amountToCharge = 110_000L;
        userPointTable.insertOrUpdate(userId, amount);

        // when & then
        Assertions.assertThatThrownBy(() -> pointService.charge(userId, amountToCharge))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("보유할 수 있는 최대 포인트(100,000원)를 초과할 수 없습니다.");

    }

    @Test
    void 특정유저_포인트_사용하기_예외_없음(){
        // given
        long userId = 6L;
        long amount = 10_000L;
        long amountToUse = 9_000L;
        long resultPoint = amount - amountToUse; // 사용 후 남은 금액
        userPointTable.insertOrUpdate(userId, amount);

        // when
        UserPoint result = pointService.use(userId, amountToUse);
        List<PointHistory> pointHistories = pointService.getHistories(userId);

        // then
        Assertions.assertThat(result.point()).isEqualTo(resultPoint);
        Assertions.assertThat(pointHistories.get(0).amount()).isEqualTo(amountToUse);
    }

    @Test
    void 특정유저_사용포인트가_0이하면_예외(){
        // given
        long userId = 7L;
        long amount = 10_000L;
        long amountToUse = 0L;
        userPointTable.insertOrUpdate(userId, amount);

        // when & then
        Assertions.assertThatThrownBy(() -> pointService.use(userId, amountToUse))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("사용 포인트는 0보다 커야 합니다.");
    }

    @Test
    void 특정유저_보유포인트보다_많이_사용하면_예외(){
        // given
        long userId = 8L;
        long amount = 10_000L;
        long amountToUse = 20_000L;
        userPointTable.insertOrUpdate(userId, amount);

        // when & then
        Assertions.assertThatThrownBy(() -> pointService.use(userId, amountToUse))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("보유 포인트보다 많이 사용할 수 없습니다.");
    }
}
