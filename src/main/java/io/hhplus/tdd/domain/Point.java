package io.hhplus.tdd.domain;

import io.hhplus.tdd.database.UserPoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Point {
    private long id;
    private long point;
    private long updateMillis;

    private static final long MAX_BALANCE = 100_000L;

    public static Point toPoint(UserPoint userPoint) {
        return Point.builder()
                .id(userPoint.id())
                .point(userPoint.point())
                .updateMillis(builder().updateMillis)
                .build();
    }

    public UserPoint toUserPoint() {
        return new UserPoint(id, point, updateMillis);
    }

    public void charge(long amount) {
        // 정책과 controller validation 혼재 되어 있음. Request 객체를 만들어서 @Valid 를 추후 프로젝트에서 사용할 예정 !!
        if (amount <= 0) throw new RuntimeException("충전 포인트는 0보다 커야 합니다.");
        if (point + amount > MAX_BALANCE) throw new RuntimeException("보유할 수 있는 최대 포인트(100,000원)를 초과할 수 없습니다.");

        this.point += amount;
    }

    public void use(long amount) {
        // 정책과 controller validation 혼재 되어 있음. Request 객체를 만들어서 @Valid 를 추후 프로젝트에서 사용할 예정 !!
        if (amount <= 0) throw new RuntimeException("사용 포인트는 0보다 커야 합니다.");
        if (point < amount) throw new RuntimeException("보유 포인트보다 많이 사용할 수 없습니다.");

        this.point -= amount;
    }
}
