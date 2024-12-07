package inhatc.cse.aurora.schedule.service;

import inhatc.cse.aurora.schedule.entity.Schedule;
import inhatc.cse.aurora.schedule.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Schedule addSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedule() {
        return scheduleRepository.findAll();
    }

}
