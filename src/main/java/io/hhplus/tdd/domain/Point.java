package io.hhplus.tdd.domain;

import io.hhplus.tdd.point.UserPoint;
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

    public static Point toPoint(UserPoint userPoint){
        return Point.builder()
                .id(userPoint.id())
                .point(userPoint.point())
                .updateMillis(builder().updateMillis)
                .build();
    }

    public UserPoint toUserPoint(){
        return new UserPoint(id,point,updateMillis);
    }

    public void charge(long amount){
        this.point += amount;
    }

    public void use(long amount){
        this.point -= amount;
    }
}
