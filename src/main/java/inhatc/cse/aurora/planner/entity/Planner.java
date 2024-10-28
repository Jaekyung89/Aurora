package inhatc.cse.aurora.planner.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "planner_data")
public class Planner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private PlannerDataType type;

    private String content;
    private Boolean isCompleted;
    private LocalTime timeSlot;
    private String color;

    // Getters and Setters

    public enum PlannerDataType {
        GOAL, TIMESTAMP, FEEDBACK
    }
}
