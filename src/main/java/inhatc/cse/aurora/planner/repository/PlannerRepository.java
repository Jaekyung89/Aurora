package inhatc.cse.aurora.planner.repository;

import inhatc.cse.aurora.planner.entity.Planner;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface PlannerRepository extends JpaRepository<Planner, Long> {
    List<Planner> findByDate(LocalDate date);
}