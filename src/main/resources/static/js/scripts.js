document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
    var addEventModal = document.getElementById('addEventModal');
    var plannerModal = document.getElementById('plannerModal');
    var addEventBtn = document.getElementById('addEventBtn');
    var closeAddModal = document.getElementsByClassName('close-add-modal')[0];
    var closePlannerModal = document.getElementsByClassName('close-planner-modal')[0];
    var form = document.getElementById('addEventForm');
    var timestampTable = document.getElementById('timestamp-table');
    var selectedColor = 'red';  // 타임스탬프 기본 색상 설정

    // FullCalendar 초기화
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        headerToolbar: {
            left: 'prev,next today',  // 이전, 다음 달 버튼
            center: 'title',
            right: ''
        },
        events: '/api/events',  // API로부터 이벤트 불러오기
        dateClick: function(info) {  // 날짜 클릭 시 Planner 모달 열기
            console.log("날짜가 클릭되었습니다:", info.dateStr);  // 콘솔 로그 추가
            plannerModal.style.display = "block";  // Planner 모달 열기
            document.getElementById('clickedDate').innerText = info.dateStr;  // 클릭한 날짜를 Planner에 표시

            // 클릭한 날짜에 맞는 일정 가져오기
            fetch(`/api/events/${info.dateStr}`)
                .then(response => response.json())
                .then(events => {
                    var eventDetails = '';
                    if (events.length === 0) {
                        eventDetails = '일정이 없습니다.';
                    } else {
                        events.forEach(event => {
                            eventDetails += `${event.title} - ${event.time}<br>`;
                        });
                    }
                    document.getElementById('schedule-content').innerHTML = eventDetails;  // 일정 표시
                })
                .catch(error => {
                    console.error('Error fetching events:', error);
                });
        }
    });

    calendar.render();

    // 일정 추가 모달 창 열기 (일정 추가 버튼 클릭 시)
    addEventBtn.onclick = function() {
        addEventModal.style.display = "block";
    };

    // 일정 추가 모달 닫기
    closeAddModal.onclick = function() {
        addEventModal.style.display = "none";
    };

    // Planner 모달 닫기
    closePlannerModal.onclick = function() {
        plannerModal.style.display = "none";
    };

    // 모달 외부 클릭 시 닫기
    window.onclick = function(event) {
        if (event.target == addEventModal) {
            addEventModal.style.display = "none";
        }
        if (event.target == plannerModal) {
            plannerModal.style.display = "none";
        }
    };

    // 타임스탬프 24시간 * 6열 생성
    for (let i = 0; i < 24 * 6; i++) {
        let div = document.createElement('div');
        let hour = Math.floor(i / 6);
        let minute = (i % 6) * 10;
        div.dataset.time = `${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`;
        div.classList.add('timestamp-cell');
        div.addEventListener('click', function() {
            this.style.backgroundColor = selectedColor;  // 선택한 색상으로 배경색 변경
        });
        timestampTable.appendChild(div);
    }

    // 컬러 팔레트에서 색상 선택
    document.querySelectorAll('.color-option').forEach(function(colorDiv) {
        colorDiv.addEventListener('click', function() {
            selectedColor = this.dataset.color;
        });
    });

    // 폼 제출 이벤트 처리 (일정 추가)
    form.onsubmit = function(event) {
        event.preventDefault();  // 기본 폼 제출 방지

        var title = document.getElementById('title').value;
        var start = document.getElementById('start').value;
        var end = document.getElementById('end').value;

        // 종료일을 하루 더 증가시키기
        var endDate = new Date(end);
        endDate.setDate(endDate.getDate() + 1);  // 종료일에 하루 더해줌
        var endDateStr = endDate.toISOString().split('T')[0];  // YYYY-MM-DD 형식으로 변환

        // 서버로 POST 요청 전송 (종료일을 하루 더해서 전송)
        fetch('/api/events/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                title: title,
                start: start,  // 시작 날짜
                end: endDateStr  // 종료일을 하루 더한 날짜로 전송
            }),
        })
            .then(response => response.json())
            .then(data => {
                console.log('Success:', data);

                // FullCalendar에 새로 추가된 이벤트를 렌더링
                calendar.addEvent({
                    title: data.title,
                    start: data.start,
                    end: data.end  // 수정된 종료일
                });

                // 모달 창 닫기
                addEventModal.style.display = "none";
                form.reset();  // 폼 초기화
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    };
});
