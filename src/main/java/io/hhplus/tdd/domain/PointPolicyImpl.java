package io.hhplus.tdd.domain;

import org.springframework.stereotype.Component;

@Component
public class PointPolicyImpl implements PointPolicy{

    private static final long MAX_BALANCE = 100_000L;

    @Override
    public void validateCharge(long currentPoint, long amountToCharge) {
        if (amountToCharge <= 0) {
            throw new RuntimeException("충전 포인트는 0보다 커야 합니다.");
        }
        if (currentPoint + amountToCharge > MAX_BALANCE) {
            throw new RuntimeException("보유할 수 있는 최대 포인트(100,000원)를 초과할 수 없습니다.");
        }
    }

    @Override
    public void validateUse(long currentPoint, long amountToUse) {
        if (amountToUse <= 0) {
            throw new RuntimeException("사용 포인트는 0보다 커야 합니다.");
        }
        if (currentPoint < amountToUse) {
            throw new RuntimeException("보유 포인트보다 많이 사용할 수 없습니다.");
        }
    }
}
