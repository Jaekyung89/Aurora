package inhatc.cse.aurora.schedule.dto;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ScheduleDTO {

    public String title;
    public String start;
    public String end;
}
