package inhatc.cse.aurora.planner.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "planner_date")  // 외래 키로 `planner_date`를 사용
    private Planner planner; // 하나의 Feedback은 하나의 Planner에 대응

    @Column(length = 1000)
    private String feedback; // 피드백 내용
}

