package point.unit;

import io.hhplus.tdd.application.PointFacade;
import io.hhplus.tdd.database.PointHistory;
import io.hhplus.tdd.database.TransactionType;
import io.hhplus.tdd.database.UserPoint;
import io.hhplus.tdd.interfaces.PointController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PointControllerUnitTest {

    @InjectMocks
    PointController pointController;

    @Mock
    PointFacade pointFacade;

    @Test
    void 특정유저_포인트_조회하기_예외_없음() {
        // given
        long userId = 1L;
        long point = 0L;
        UserPoint userPoint = new UserPoint(userId, point, System.currentTimeMillis());

        Mockito.when(pointFacade.getPoint(userId)).thenReturn(userPoint);

        // when
        UserPoint result = pointController.point(userId);

        // then
        Assertions.assertThat(result.point()).isEqualTo(point);
    }

    @Test
    void 특정유저_포인트_조회하기_ID_NPE_예외(){
        //given
        Long userId = null;

        // when & then
        Assertions.assertThatThrownBy(()->pointController.point(userId))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 특정유저_포인트_충전_이용내역_조회하기_예외_없음() {
        // given
        long userId = 1L;
        long amount = 5_000L;
        List<PointHistory> historyList = new ArrayList<>();
        PointHistory pointHistory = new PointHistory(1, userId, amount, TransactionType.CHARGE, System.currentTimeMillis());
        historyList.add(pointHistory);

        Mockito.when(pointFacade.getHistories(userId)).thenReturn(historyList);

        // when
        List<PointHistory> result = pointController.history(userId);

        // then
        Assertions.assertThat(result.get(0).amount()).isEqualTo(amount);
    }

    @Test
    void 특정유저_포인트_충전_이용내역_조회하기_ID_NPE_예외(){
        //given
        Long userId = null;

        // when & then
        Assertions.assertThatThrownBy(()->pointController.history(userId))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 특정유저_포인트_충전하기_예외_없음(){
        // given
        long userId = 1L;
        long amountToCharge = 5_000L;
        UserPoint userPoint = new UserPoint(userId, amountToCharge, System.currentTimeMillis());

        Mockito.when(pointFacade.charge(userId,amountToCharge)).thenReturn(userPoint);

        // when
        UserPoint result = pointController.charge(userId, amountToCharge);

        // then
        Assertions.assertThat(result.point()).isEqualTo(amountToCharge);
    }

    @Test
    void 특정유저_포인트_충전하기_ID_NPE_예외(){
        //given
        Long userId = null;
        long amount = 5_000L;

        // when & then
        Assertions.assertThatThrownBy(()->pointController.charge(userId, amount))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 특정유저_포인트_충전하기_AMOUNT_NPE_예외(){
        //given
        long userId = 1L;
        Long amount = null;

        // when & then
        Assertions.assertThatThrownBy(()->pointController.charge(userId, amount))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 특정유저_포인트_사용하기_예외_없음(){
        // given
        long userId = 1L;
        long amount = 5_000L;
        long amountToUse = 5_000L;
        UserPoint userPoint = new UserPoint(userId, amount, System.currentTimeMillis());

        Mockito.when(pointFacade.use(userId,amountToUse)).thenReturn(userPoint);

        // when
        UserPoint result = pointController.use(userId, amountToUse);

        // then
        Assertions.assertThat(result.point()).isEqualTo(amount);
    }

    @Test
    void 특정유저_포인트_사용하기_ID__NPE_예외(){
        //given
        Long userId = null;
        long amount = 5_000L;

        // when & then
        Assertions.assertThatThrownBy(()->pointController.use(userId, amount))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 특정유저_포인트_사용하기_AMOUNT__NPE_예외(){
        //given
        long userId = 1L;
        Long amount = null;

        // when & then
        Assertions.assertThatThrownBy(()->pointController.use(userId, amount))
                .isInstanceOf(NullPointerException.class);
    }


}
