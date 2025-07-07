package point;

import io.hhplus.tdd.domain.PointPolicy;
import io.hhplus.tdd.domain.PointPolicyImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PointPolicyUnitTest {

    PointPolicy pointPolicy = new PointPolicyImpl();

    @Test
    void 포인트_음수_충전시_예외_발생_및_메시지_검증(){
        // given
        long amount = -5000;

        // when & then
        Assertions.assertThatThrownBy(()-> pointPolicy.validateCharge(amount))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("충전 포인트는 0보다 커야 합니다.");
    }

    @Test
    void 포인트충전_정상적인_사용은_예외_없음() {
        // given
        long amount = 1;

        // when & then
        Assertions.assertThatCode(() -> pointPolicy.validateCharge(amount))
                .doesNotThrowAnyException();

    }

    @Test
    void 사용포인트가_0이하이면_예외() {
        // given
        long currentPoint = 1000L;
        long amountToUse = 0L;

        // when & then
        Assertions.assertThatThrownBy(() -> pointPolicy.validateUse(currentPoint, amountToUse))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("사용 포인트는 0보다 커야 합니다.");
    }

    @Test
    void 보유포인트보다_많이_사용하면_예외() {
        // given
        long currentPoint = 3000L;
        long amountToUse = 5000L;

        // when & then
        Assertions.assertThatThrownBy(() -> pointPolicy.validateUse(currentPoint, amountToUse))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("보유 포인트보다 많이 사용할 수 없습니다.");
    }

    @Test
    void 포인트사용_정상적인_사용은_예외_없음() {
        // given
        long currentPoint = 10000L;
        long amountToUse = 5000L;

        // when & then
        Assertions.assertThatCode(() -> pointPolicy.validateUse(currentPoint, amountToUse))
                .doesNotThrowAnyException();
    }

}
