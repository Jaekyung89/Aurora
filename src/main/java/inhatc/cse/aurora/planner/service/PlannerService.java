package inhatc.cse.aurora.planner.service;

import inhatc.cse.aurora.planner.entity.Planner;
import inhatc.cse.aurora.planner.repository.PlannerRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import java.util.List;

@Service
public class PlannerService {

    @Autowired
    private PlannerRepository plannerDataRepository;

    public List<Planner> getDataByDate(LocalDate date) {
        return plannerDataRepository.findByDate(date);
    }

    public Planner saveData(Planner plannerData) {
        return plannerDataRepository.save(plannerData);
    }

    public void saveAllData(List<Planner> dataList) {
        plannerDataRepository.saveAll(dataList);
    }
}