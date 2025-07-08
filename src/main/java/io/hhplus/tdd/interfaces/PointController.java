package io.hhplus.tdd.interfaces;

import io.hhplus.tdd.application.PointFacade;
import io.hhplus.tdd.database.PointHistory;
import io.hhplus.tdd.database.UserPoint;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {

    private final PointFacade pointFacade;


    private static final Logger log = LoggerFactory.getLogger(PointController.class);

    /**
     * TODO - 특정 유저의 포인트를 조회하는 기능을 작성해주세요.
     */
    @GetMapping("/{id}")
    public UserPoint point(
            @PathVariable long id
    ) {
        return pointFacade.getPoint(id);
    }

    /**
     * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
     */
    @GetMapping("/{id}/histories")
    public List<PointHistory> history(
            @PathVariable long id
    ) {
        return pointFacade.getHistories(id);
    }

    /**
     * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
     */
    @PatchMapping("/{id}/charge")
    public UserPoint charge(
            @PathVariable long id,
            @RequestBody long amount
    ) {
        return pointFacade.charge(id, amount);
    }

    /**
     * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     */
    @PatchMapping("/{id}/use")
    public UserPoint use(
            @PathVariable long id,
            @RequestBody long amount
    ) {
        return pointFacade.use(id, amount);
    }
}
