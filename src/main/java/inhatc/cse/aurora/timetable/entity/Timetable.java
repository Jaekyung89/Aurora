package inhatc.cse.aurora.timetable.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import inhatc.cse.aurora.goal.constant.GoalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    @Column(name = "goal_id")
    private Long id;

}
