package io.hhplus.tdd.domain;

public interface PointPolicy {

    void validateCharge(long currentPoint, long amountToCharge);

    void validateUse(long currentPoint, long amountToUse);
}
