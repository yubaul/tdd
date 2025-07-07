package io.hhplus.tdd.domain;

public interface PointPolicy {

    void validateCharge(long amount);

    void validateUse(long currentPoint, long amountToUse);
}
