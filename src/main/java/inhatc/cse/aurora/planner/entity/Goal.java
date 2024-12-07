package inhatc.cse.aurora.planner.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import inhatc.cse.aurora.planner.constant.GoalStatus;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "planner_date")  // 외래 키로 `planner_date`를 사용
    private Planner planner; // 하나의 Goal은 하나의 Planner에 대응

    @Enumerated(EnumType.STRING)
    private GoalStatus status; // 목표 달성 여부

    @Column(length = 1000)
    private String goal; // 목표 내용
}
