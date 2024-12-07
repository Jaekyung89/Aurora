package inhatc.cse.aurora.planner.dto;

import inhatc.cse.aurora.planner.entity.Feedback;
import inhatc.cse.aurora.planner.entity.Goal;
import inhatc.cse.aurora.planner.entity.Timetable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlannerDTO {
    private String date;
    private String timetable;
    private String feedback;
    private List<String> goals;  // 여러 목표를 받는 리스트
    private List<Boolean> goalCompletions;  // 목표 완료 여부
    private List<String> timestampColors;  // 타임스탬프 색상 목록
    private List<String> timestamps;  // 타임스탬프 시간 목록
}
