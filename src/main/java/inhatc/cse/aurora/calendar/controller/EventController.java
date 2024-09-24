package inhatc.cse.aurora.calendar.controller;

import inhatc.cse.aurora.calendar.service.EventService;
import inhatc.cse.aurora.calendar.entity.Event;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // 이벤트 추가
    @PostMapping("/add")
    public ResponseEntity<Event> addEvent(@RequestBody Event event) {
        Event newEvent = eventService.addEvent(event);
        return new ResponseEntity<>(newEvent, HttpStatus.CREATED);
    }

    // 모든 이벤트 조회
    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }
}

