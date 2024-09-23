package inhatc.cse.aurora.calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Getter
    @Setter
    private LocalDate start;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Getter
    @Setter
    private LocalDate end;

    @Getter
    @Setter
    private String url;

    public Event(String title, LocalDateTime parse, LocalDateTime parse1, String url) {
    }

    public Event() {

    }
}