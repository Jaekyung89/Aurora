package inhatc.cse.aurora.planner.repository;

import inhatc.cse.aurora.planner.entity.Goal;
import inhatc.cse.aurora.planner.entity.Planner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByPlanner(Planner planner);
    void deleteByPlanner(Planner planner);
}