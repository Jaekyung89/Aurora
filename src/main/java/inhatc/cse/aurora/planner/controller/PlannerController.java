package inhatc.cse.aurora.planner.controller;

import inhatc.cse.aurora.planner.entity.Planner;
import inhatc.cse.aurora.planner.service.PlannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/planner")
public class PlannerController {

    @Autowired
    private PlannerService plannerService;

    @GetMapping("/getData")
    public List<Planner> getData(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        return plannerService.getDataByDate(localDate);
    }

    @PostMapping("/saveData")
    public ResponseEntity<?> saveData(@RequestBody List<Planner> data) {
        if (data.stream().anyMatch(item -> item.getDate() == null)) {
            return ResponseEntity.badRequest().body("Date field cannot be null.");
        }
        plannerService.saveAllData(data);
        return ResponseEntity.ok().body(Map.of("success", true));
    }
}
