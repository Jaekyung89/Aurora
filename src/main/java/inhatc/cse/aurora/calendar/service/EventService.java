package inhatc.cse.aurora.calendar.service;

import inhatc.cse.aurora.calendar.entity.Event;
import inhatc.cse.aurora.calendar.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // 이벤트 추가
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    // 모든 이벤트 조회
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

}
