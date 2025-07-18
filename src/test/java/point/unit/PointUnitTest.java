package point.unit;

import io.hhplus.tdd.domain.Point;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PointUnitTest {


    @Test
    void 포인트_음수_충전시_예외_발생_및_메시지_검증(){
        // given
        long currentPoint = 50_000L;
        long amountToCharge = -5_000L;
        long userId = 1L;
        Point point = Point.builder()
                .id(userId)
                .point(currentPoint)
                .updateMillis(System.currentTimeMillis())
                .build();

        // when & then
        Assertions.assertThatThrownBy(()-> point.charge(amountToCharge))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("충전 포인트는 0보다 커야 합니다.");
    }
    @Test
    void 최대포인트_초과_충전시_예외_발생() {
        // given
        long currentPoint = 95_000L;
        long amountToCharge = 10_000L; // 합계 105,000 → 초과
        long userId = 1L;
        Point point = Point.builder()
                .id(userId)
                .point(currentPoint)
                .updateMillis(System.currentTimeMillis())
                .build();

        // when & then
        Assertions.assertThatThrownBy(() -> point.charge(amountToCharge))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("보유할 수 있는 최대 포인트(100,000원)를 초과할 수 없습니다.");
    }

    @Test
    void 포인트충전_정상적인_사용은_예외_없음() {
        // given
        long currentPoint = 50_000L;
        long amountToCharge = 1L;
        long userId = 1L;
        Point point = Point.builder()
                .id(userId)
                .point(currentPoint)
                .updateMillis(System.currentTimeMillis())
                .build();

        // when & then
        Assertions.assertThatCode(() -> point.charge(amountToCharge))
                .doesNotThrowAnyException();

    }

    @Test
    void 사용포인트가_0이하이면_예외() {
        // given
        long currentPoint = 1_000L;
        long amountToUse = 0L;
        long userId = 1L;
        Point point = Point.builder()
                .id(userId)
                .point(currentPoint)
                .updateMillis(System.currentTimeMillis())
                .build();

        // when & then
        Assertions.assertThatThrownBy(() -> point.use(amountToUse))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("사용 포인트는 0보다 커야 합니다.");
    }

    @Test
    void 보유포인트보다_많이_사용하면_예외() {
        // given
        long currentPoint = 3_000L;
        long amountToUse = 5_000L;
        long userId = 1L;
        Point point = Point.builder()
                .id(userId)
                .point(currentPoint)
                .updateMillis(System.currentTimeMillis())
                .build();

        // when & then
        Assertions.assertThatThrownBy(() -> point.use(amountToUse))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("보유 포인트보다 많이 사용할 수 없습니다.");
    }

    @Test
    void 포인트사용_정상적인_사용은_예외_없음() {
        // given
        long currentPoint = 10_000L;
        long amountToUse = 5_000L;
        long userId = 1L;
        Point point = Point.builder()
                .id(userId)
                .point(currentPoint)
                .updateMillis(System.currentTimeMillis())
                .build();

        // when & then
        Assertions.assertThatCode(() -> point.use(amountToUse))
                .doesNotThrowAnyException();
    }

}
