package inhatc.cse.aurora.planner.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timetable_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "planner_date")  // 외래 키로 `planner_date`를 사용
    private Planner planner; // 하나의 Timetable은 하나의 Planner에 대응

    @Column(length = 1000)
    private String timestampColors; // 색상 배열을 문자열로 저장 (예: "R,G,B,...")

    @Column(length = 1000)
    private String timestamps;    // 타임스탬프 배열을 문자열로 저장 (예: "00:00,00:10,...")
}