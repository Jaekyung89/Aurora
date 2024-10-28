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
        dateClick: function(info) {
            plannerModal.style.display = "block";
            document.getElementById('clickedDate').innerText = info.dateStr;

            fetch(`/api/events/${info.dateStr}`)
                .then(response => response.json())
                .then(events => {
                    if (Array.isArray(events) && events.length > 0) {
                        let eventDetails = events.map(event => `${event.title}`).join("<br>");
                        document.getElementById('schedule-content').innerHTML = eventDetails;
                    } else {
                        document.getElementById('schedule-content').innerHTML = "일정이 없습니다.";
                    }
                })
                .catch(error => {
                    console.error("Error fetching events:", error);
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

    // Planner 모달 닫기 (데이터 저장 후 닫기)
    closePlannerModal.onclick = function() {
        saveData(); // 모달 닫기 전에 데이터 저장
        plannerModal.style.display = "none";
    };

    // 모달 외부 클릭 시 닫기 (데이터 저장 후 닫기)
    window.onclick = function(event) {
        if (event.target == addEventModal) {
            addEventModal.style.display = "none";
        }
        if (event.target == plannerModal) {
            saveData(); // 모달 닫기 전에 데이터 저장
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
            if (div.style.backgroundColor === selectedColor) {
                div.style.backgroundColor = '#f9f9f9';  // 기본 색상으로 초기화
            } else {
                div.style.backgroundColor = selectedColor;
            }
        });
        timestampTable.appendChild(div);
    }

    // 날짜별 데이터를 불러오는 함수
    function loadData(date) {
        fetch(`/api/planner/getData?date=${date}`)
            .then(response => response.json())
            .then(data => {
                document.querySelectorAll('.goal-input').forEach((input, idx) => {
                    const goal = data.find(item => item.type === 'GOAL' && item.goalOrder === idx);
                    if (goal) {
                        input.value = goal.content;
                        input.nextElementSibling.checked = goal.isCompleted;
                    }
                });

                document.getElementById('feedbackInput').value = data.find(item => item.type === 'FEEDBACK')?.content || '';

                document.querySelectorAll('.timestamp-cell').forEach((cell) => {
                    const timestamp = data.find(item => item.type === 'TIMESTAMP' && item.timeSlot === cell.dataset.time);
                    cell.style.backgroundColor = timestamp ? timestamp.color : '#FFFFFF';
                });
            })
            .catch(error => console.error('Error fetching data:', error));
    }

    function saveData() {
        const date = document.getElementById('clickedDate').innerText;
        if (!date) {
            console.error('Date is missing. Cannot save data.');
            return;
        }

        const goalData = Array.from(document.querySelectorAll('.goal-input')).map((input, idx) => ({
            date,
            type: 'GOAL',
            content: input.value,
            isCompleted: input.nextElementSibling.checked
        }));

        const feedbackData = {
            date,
            type: 'FEEDBACK',
            content: document.getElementById('feedbackInput').value
        };

        const timestampData = Array.from(document.querySelectorAll('.timestamp-cell')).map(cell => ({
            date,
            type: 'TIMESTAMP',
            timeSlot: cell.dataset.time,
            color: cell.style.backgroundColor || '#FFFFFF'
        }));

        const allData = [...goalData, feedbackData, ...timestampData];

        fetch('/api/planner/saveData', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(allData),
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    console.log('Data saved successfully:', data);
                } else {
                    console.error('Data saving failed:', data);
                }
            })
            .catch(error => console.error('Error saving data:', error));
    }



    // 컬러 팔레트에서 색상 선택
    document.querySelectorAll('.color-option').forEach(function(colorDiv) {
        colorDiv.addEventListener('click', function() {
            selectedColor = this.dataset.color;
        });
    });

    // 폼 제출 이벤트 처리 (일정 추가)
    form.onsubmit = function(event) {
        event.preventDefault();

        var title = document.getElementById('title').value;
        var start = document.getElementById('start').value;
        var end = document.getElementById('end').value;

        // 종료일을 하루 더 증가시키기
        var endDate = new Date(end);
        endDate.setDate(endDate.getDate() + 1);
        var endDateStr = endDate.toISOString().split('T')[0];

        fetch('/api/events/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                title: title,
                start: start,
                end: endDateStr
            }),
        })
            .then(response => response.json())
            .then(data => {
                console.log('Event added:', data);

                calendar.addEvent({
                    title: data.title,
                    start: data.start,
                    end: data.end
                });

                addEventModal.style.display = "none";
                form.reset();
            })
            .catch((error) => {
                console.error('Error adding event:', error);
            });
    };
});
