package inhatc.cse.aurora.planner.controller;

import inhatc.cse.aurora.planner.dto.PlannerDTO;
import inhatc.cse.aurora.planner.entity.Planner;
import inhatc.cse.aurora.planner.service.PlannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/planner") // 데이터를 가져오고 저장하는 API는 이 경로를 사용
public class PlannerController {

    @Autowired
    private PlannerService plannerService;

    // 특정 날짜의 플래너 데이터를 가져오는 엔드포인트
    @GetMapping("/{date}")
    public ResponseEntity<PlannerDTO> getPlannerByDate(@PathVariable String date) {
        PlannerDTO plannerDTO = plannerService.getPlannerDataByDate(date);

        if (plannerDTO != null) {
            return ResponseEntity.ok(plannerDTO); // 데이터 반환
        } else {
            return ResponseEntity.noContent().build(); // 데이터가 없을 경우 204 No Content 반환
        }
    }

    // 플래너 데이터를 저장하는 엔드포인트
    @PostMapping
    public ResponseEntity<?> savePlannerData(@RequestBody PlannerDTO data) {
        try {
            // 데이터 저장
            plannerService.savePlannerData(data);
            return ResponseEntity.ok(new HashMap<String, String>() {{
                put("success", "true");
            }});
        } catch (Exception e) {
            // 에러 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new HashMap<String, String>() {{
                        put("success", "false");
                    }});
        }
    }
}