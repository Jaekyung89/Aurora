package inhatc.cse.aurora.planner.repository;

import inhatc.cse.aurora.planner.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {
}