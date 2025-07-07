package io.hhplus.tdd.domain;

import org.springframework.stereotype.Component;

@Component
public class PointPolicyImpl implements PointPolicy{
    @Override
    public void validateCharge(long amount) {
        if (amount <= 0) {
            throw new RuntimeException("충전 포인트는 0보다 커야 합니다.");
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
