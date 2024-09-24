package inhatc.cse.aurora.calendar.dto;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class EventDTO {

    public String title;
    public String start;
    public String end;
    public String url;
}
