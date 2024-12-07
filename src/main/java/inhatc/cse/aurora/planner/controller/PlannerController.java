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
@RequestMapping("/save-planner")
public class PlannerController {

    @Autowired
    private PlannerService plannerService;

    @PostMapping
    public ResponseEntity<?> savePlannerData(@RequestBody PlannerDTO data) {
        try {
            // 데이터베이스에 저장
            plannerService.savePlannerData(data);
            return ResponseEntity.ok(new HashMap<String, String>() {{
                put("success", "true");
            }});
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new HashMap<String, String>() {{
                        put("success", "false");
                    }});
        }
    }
}
