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

import java.util.Optional;

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

    public void savePlannerData(PlannerDTO data) {
        System.out.println("Received Date: " + data.getDate());
        // Planner 엔티티 저장
        Planner planner = new Planner();
        planner.setDate(data.getDate());
        plannerRepository.save(planner);

        // Feedback 저장
        Feedback feedback = new Feedback();
        feedback.setFeedback(data.getFeedback());
        feedback.setPlanner(planner);
        feedbackRepository.save(feedback);

        // Timetable 저장
        Timetable timetable = new Timetable();
        timetable.setTimestampColors(data.getTimetable());  // 색상 배열
        timetable.setPlanner(planner);
        timetable.setTimestamps(String.join(",", data.getTimestamps()));  // 시간 배열을 문자열로 저장
        timetable.setTimestampColors(String.join(",", data.getTimestampColors()));  // 색상 배열을 문자열로 저장
        timetableRepository.save(timetable);

        // Goal이 비어 있지 않으면 저장
        if (data.getGoals() != null && !data.getGoals().isEmpty()) {
            for (int i = 0; i < data.getGoals().size(); i++) {
                String goalText = data.getGoals().get(i);
                Boolean goalCompletion = data.getGoalCompletions().get(i);

                if (goalText != null && !goalText.trim().isEmpty()) {
                    Goal goal = new Goal();
                    goal.setGoal(goalText);
                    goal.setStatus(goalCompletion ? GoalStatus.COMPLETE : GoalStatus.INCOMPLETE); // GoalStatus 처리
                    goal.setPlanner(planner);
                    goalRepository.save(goal);
                }
            }
        }
    }
}
