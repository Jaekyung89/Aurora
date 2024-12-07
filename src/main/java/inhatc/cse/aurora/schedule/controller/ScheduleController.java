package inhatc.cse.aurora.schedule.controller;

import inhatc.cse.aurora.schedule.service.ScheduleService;
import inhatc.cse.aurora.schedule.entity.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // 이벤트 추가
    @PostMapping("/add")
    public ResponseEntity<Schedule> addEvent(@RequestBody Schedule schedule) {
        Schedule newSchedule = scheduleService.addSchedule(schedule);
        return new ResponseEntity<>(newSchedule, HttpStatus.CREATED);
    }

    // 모든 이벤트 조회
    @GetMapping
    public List<Schedule> getAllSchedule() {
        return scheduleService.getAllSchedule();
    }

}
