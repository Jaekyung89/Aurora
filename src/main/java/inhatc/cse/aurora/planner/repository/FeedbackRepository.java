package inhatc.cse.aurora.planner.repository;

import inhatc.cse.aurora.planner.entity.Feedback;
import inhatc.cse.aurora.planner.entity.Planner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Feedback findByPlanner(Planner planner);
}