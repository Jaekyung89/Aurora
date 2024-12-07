package inhatc.cse.aurora.schedule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate start;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;

}