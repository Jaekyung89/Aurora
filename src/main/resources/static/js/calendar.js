$(document).ready(function() {
    $('#calendar').fullCalendar({
        events: '/api/events',  // API로부터 이벤트 목록을 불러오기
        selectable: true,
        selectHelper: true,
        select: function(start, end) {
            var title = prompt('Event Title:');
            if (title) {
                var eventData = {
                    title: title,
                    start: info.startStr,  // 문자열 형식으로 날짜 전송 (ISO 8601 형식)
                    end: info.endStr       // 문자열 형식으로 날짜 전송
                };
                $.ajax({
                    url: '/api/events/add',
                    method: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(eventData),
                    success: function() {
                        $('#calendar').fullCalendar('renderEvent', eventData, true);
                    }
                });
            }
            $('#calendar').fullCalendar('unselect');
        }
    });
});
