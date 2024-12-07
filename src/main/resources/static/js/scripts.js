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
            selectedDate = info.dateStr; // 클릭한 날짜를 변수에 저장
            document.getElementById('clickedDate').innerText = selectedDate;
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
        saveData();
        plannerModal.style.display = "none";
    };

    // 모달 외부 클릭 시 닫기 (데이터 저장 후 닫기)
    window.onclick = function(event) {
        if (event.target == addEventModal) {
            addEventModal.style.display = "none";
        }
        if (event.target == plannerModal) {
            saveData();
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

    // 컬러 팔레트에서 색상 선택
    document.querySelectorAll('.color-option').forEach(function (colorDiv) {
        colorDiv.addEventListener('click', function () {
            selectedColor = this.dataset.color; // 선택된 색상으로 변경
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

    //플래너 저장
    function saveData() {
        const timetableData = document.getElementById('timetable') ? document.getElementById('timetable').value : '';
        const feedbackData = document.getElementById('feedbackInput') ? document.getElementById('feedbackInput').value : '';
        const goalData = [];
        const goalCompletions = [];

        // 목표 칸 여러 개 처리 (예: Goal1, Goal2, ...)
        for (let i = 1; i <= 6; i++) {  // 목표가 6개인 경우
            const goalInput = document.getElementById('Goal' + i);
            if (goalInput && goalInput.value.trim() !== "") {
                goalData.push(goalInput.value);  // 비어 있지 않으면 목표 추가
                const goalCompletion = document.getElementById('GoalCheck' + i) ? document.getElementById('GoalCheck' + i).checked : false;
                goalCompletions.push(goalCompletion); // 목표 완료 여부 추가
            }
        }

        // 타임스탬프 색상 수집
        const timestampColors = [];
        const timestamps = [];
        const timestampCells = document.querySelectorAll('.timestamp-cell');
        timestampCells.forEach(cell => {
            if (cell.style.backgroundColor && cell.style.backgroundColor !== 'rgb(249, 249, 249)') {  // 기본 색상이 아닌 경우
                timestampColors.push(cell.style.backgroundColor);  // 색상 추가
                timestamps.push(cell.dataset.time);  // 시간 추가
            }
        });

        // 목표가 비어 있지 않으면 목표와 목표 완료 여부를 포함하여 전송
        if (goalData.length > 0 || timestampColors.length > 0) {  // 목표나 타임스탬프가 있으면
            const data = {
                timetable: timetableData,
                feedback: feedbackData,
                goals: goalData,           // 목표 목록
                goalCompletions: goalCompletions, // 목표 완료 여부 목록
                timestampColors: timestampColors, // 타임스탬프 색상 목록
                timestamps: timestamps, // 타임스탬프 시간 목록
                date: selectedDate
            };

            // 서버로 데이터 전송 (예: POST 요청)
            fetch('/save-planner', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert('데이터가 성공적으로 저장되었습니다!');
                    } else {
                        alert('저장 실패');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('저장 중 오류가 발생했습니다.');
                });
        } else {
            alert('목표를 입력해주세요.');
        }
    }
});
