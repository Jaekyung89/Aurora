package inhatc.cse.aurora.feedback.entity;

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
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JoinColumn(name = "start")
    private LocalDate date;

    private String feedback;
}
