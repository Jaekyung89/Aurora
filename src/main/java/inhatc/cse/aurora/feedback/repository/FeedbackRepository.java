package inhatc.cse.aurora.feedback.repository;

import inhatc.cse.aurora.feedback.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
