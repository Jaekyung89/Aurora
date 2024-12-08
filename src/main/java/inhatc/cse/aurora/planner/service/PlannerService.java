package inhatc.cse.aurora.planner.service;

import inhatc.cse.aurora.planner.constant.GoalStatus;
import inhatc.cse.aurora.planner.dto.PlannerDTO;
import inhatc.cse.aurora.planner.entity.Feedback;
import inhatc.cse.aurora.planner.entity.Goal;
import inhatc.cse.aurora.planner.entity.Planner;
import inhatc.cse.aurora.planner.entity.Timetable;
import inhatc.cse.aurora.planner.repository.FeedbackRepository;
import inhatc.cse.aurora.planner.repository.GoalRepository;
import inhatc.cse.aurora.planner.repository.PlannerRepository;
import inhatc.cse.aurora.planner.repository.TimetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlannerService {

    @Autowired
    private PlannerRepository plannerRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    private TimetableRepository timetableRepository;

    public PlannerDTO getPlannerDataByDate(String date) {
        Optional<Planner> optionalPlanner = plannerRepository.findById(date);

        if (optionalPlanner.isPresent()) {
            Planner planner = optionalPlanner.get();
            PlannerDTO plannerDTO = new PlannerDTO();

            // Planner 기본 정보 설정
            plannerDTO.setDate(planner.getDate());

            // Feedback 설정
            Feedback feedback = feedbackRepository.findByPlanner(planner);
            plannerDTO.setFeedback(feedback != null ? feedback.getFeedback() : "");

            // Timetable 설정
            Timetable timetable = timetableRepository.findByPlanner(planner);
            if (timetable != null) {
                plannerDTO.setTimestampColors(
                        timetable.getTimestampColors() != null ? Arrays.asList(timetable.getTimestampColors().split(",")) : new ArrayList<>()
                );
                plannerDTO.setTimestamps(
                        timetable.getTimestamps() != null ? Arrays.asList(timetable.getTimestamps().split(",")) : new ArrayList<>()
                );
            } else {
                plannerDTO.setTimestampColors(new ArrayList<>());
                plannerDTO.setTimestamps(new ArrayList<>());
            }

            // Goals 설정
            List<Goal> goals = goalRepository.findByPlanner(planner);
            if (goals != null && !goals.isEmpty()) {
                plannerDTO.setGoals(goals.stream().map(Goal::getGoal).collect(Collectors.toList()));
                plannerDTO.setGoalCompletions(goals.stream().map(goal -> goal.getStatus() == GoalStatus.COMPLETE).collect(Collectors.toList()));
            } else {
                plannerDTO.setGoals(new ArrayList<>());
                plannerDTO.setGoalCompletions(new ArrayList<>());
            }

            // 디버깅용 로그
            System.out.println("Planner Data Retrieved: " + plannerDTO);

            return plannerDTO;
        }

        return null; // 데이터가 없는 경우 null 반환
    }
    @Transactional
    public void savePlannerData(PlannerDTO data) {
        // Planner 가져오기 또는 생성
        Planner planner = plannerRepository.findById(data.getDate())
                .orElseGet(() -> {
                    Planner newPlanner = new Planner();
                    newPlanner.setDate(data.getDate());
                    return plannerRepository.save(newPlanner);
                });

        // Feedback 저장 또는 업데이트
        Feedback feedback = feedbackRepository.findByPlanner(planner);
        if (feedback == null) {
            feedback = new Feedback();
            feedback.setPlanner(planner);
        }
        feedback.setFeedback(data.getFeedback());
        feedbackRepository.save(feedback);

        // Timetable 저장 또는 업데이트
        Timetable timetable = timetableRepository.findByPlanner(planner);
        if (timetable == null) {
            timetable = new Timetable();
            timetable.setPlanner(planner);
        }
        timetable.setTimestamps(String.join(",", data.getTimestamps()));
        timetable.setTimestampColors(String.join(",", data.getTimestampColors()));
        timetableRepository.save(timetable);

        // 기존 목표 삭제 및 새로운 목표 저장
        goalRepository.deleteByPlanner(planner);
        goalRepository.flush();
        if (data.getGoals() != null && !data.getGoals().isEmpty()) {
            for (int i = 0; i < data.getGoals().size(); i++) {
                Goal goal = new Goal();
                goal.setPlanner(planner);
                goal.setGoal(data.getGoals().get(i));
                goal.setStatus(data.getGoalCompletions().get(i) ? GoalStatus.COMPLETE : GoalStatus.INCOMPLETE);
                goalRepository.save(goal);
            }
        }
    }
}
