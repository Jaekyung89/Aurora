package inhatc.cse.aurora.goal.repository;

import inhatc.cse.aurora.goal.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface GoalRepository extends JpaRepository<Goal, Long> {
}
