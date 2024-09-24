package inhatc.cse.aurora.calendar.repository;

import inhatc.cse.aurora.calendar.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByStartBetween(LocalDateTime start, LocalDateTime end);
}