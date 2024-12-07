package inhatc.cse.aurora.planner.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Planner {

    @Id
    @Column(nullable = false, unique = true)
    private String date; // 기본 키 (YYYY-MM-DD 형식)

    @OneToOne(mappedBy = "planner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Feedback feedback; // 하나의 Planner에 하나의 Feedback

    @OneToMany(mappedBy = "planner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Goal> goals; // 하나의 Planner에 하나의 Goal

    @OneToOne(mappedBy = "planner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Timetable timetable; // 하나의 Planner에 하나의 Timetable
}
