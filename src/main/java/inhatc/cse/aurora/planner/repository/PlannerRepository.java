package inhatc.cse.aurora.planner.repository;

import inhatc.cse.aurora.planner.entity.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlannerRepository extends JpaRepository<Planner, String> {
    Optional<Planner> findByDate(String date);
}