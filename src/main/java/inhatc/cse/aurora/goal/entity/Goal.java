package inhatc.cse.aurora.goal.entity;

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
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JoinColumn(name = "start")
    private LocalDate date;

    private String goal;

    private GoalStatus completion;
}
