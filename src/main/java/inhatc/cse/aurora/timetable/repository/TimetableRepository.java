package inhatc.cse.aurora.timetable.repository;

import inhatc.cse.aurora.timetable.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
}