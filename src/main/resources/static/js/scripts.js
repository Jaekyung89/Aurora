document.addEventListener('DOMContentLoaded', function () {
    var calendarEl = document.getElementById('calendar');
    var addEventModal = document.getElementById('addEventModal');
    var plannerModal = document.getElementById('plannerModal');
    var addEventBtn = document.getElementById('addEventBtn');
    var closeAddModal = document.getElementsByClassName('close-add-modal')[0];
    var closePlannerModal = document.getElementsByClassName('close-planner-modal')[0];
    var form = document.getElementById('addEventForm');
    var timestampTable = document.getElementById('timestamp-table');
    var selectedColor = 'red'; // 타임스탬프 기본 색상 설정
    var selectedDate = ''; // 선택된 날짜

    // FullCalendar 초기화
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        headerToolbar: {
            left: 'prev,next today', // 이전, 다음 달 버튼
            center: 'title',
            right: ''
        },

        events: '/api/events', // API로부터 이벤트 불러오기
        dateClick: function (info) {
            plannerModal.style.display = 'block';
            selectedDate = info.dateStr; // 클릭한 날짜를 변수에 저장
            document.getElementById('clickedDate').innerText = selectedDate;
            fetchPlannerData(selectedDate); // 데이터를 서버에서 불러오기
        }
    });

    calendar.render();

    // 일정 추가 모달 창 열기 (일정 추가 버튼 클릭 시)
    addEventBtn.onclick = function () {
        addEventModal.style.display = 'block';
    };

    // 일정 추가 모달 닫기
    closeAddModal.onclick = function () {
        addEventModal.style.display = 'none';
    };

    // Planner 모달 닫기 (데이터 저장 후 닫기)
    closePlannerModal.onclick = function () {
        saveData();
        plannerModal.style.display = 'none';
    };

    // 모달 외부 클릭 시 닫기 (데이터 저장 후 닫기)
    window.onclick = function (event) {
        if (event.target == addEventModal) {
            addEventModal.style.display = 'none';
        }
        if (event.target == plannerModal) {
            saveData();
            plannerModal.style.display = 'none';
        }
    };

    // 타임스탬프 24시간 * 6열 생성 (시간 추가)
    function renderTimestampTable() {
        const timestampTable = document.getElementById('timestamp-table');
        timestampTable.innerHTML = ''; // Clear existing content

        for (let hour = 0; hour < 24; hour++) {
            const row = document.createElement('div');
            row.className = 'timestamp-row';

            // 시간 레이블
            const timeLabel = document.createElement('div');
            timeLabel.className = 'timestamp-time';
            timeLabel.textContent = `${hour.toString().padStart(2, '0')}:00`;
            row.appendChild(timeLabel);

            // 10분 단위의 셀 생성
            for (let i = 0; i < 6; i++) {
                const div = document.createElement('div');
                const minute = i * 10;
                div.dataset.time = `${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`;
                div.classList.add('timestamp-cell');
                div.addEventListener('click', function () {
                    if (div.style.backgroundColor === selectedColor) {
                        div.style.backgroundColor = '#f9f9f9'; // Reset to default
                    } else {
                        div.style.backgroundColor = selectedColor; // Apply selected color
                    }
                });
                row.appendChild(div);
            }

            timestampTable.appendChild(row);
        }
    }

    renderTimestampTable();



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

    // Planner 데이터를 불러오는 함수
    function fetchPlannerData(date) {
        resetPlannerModal(); // 기존 데이터 초기화

        fetch(`/api/planner/${date}`)
            .then(response => {
                if (response.status === 204) {
                    console.log('No data for selected date (204 No Content)');
                    return null;
                } else if (response.status === 404) {
                    console.error('API endpoint not found (404 Not Found)');
                    return null;
                } else if (!response.ok) {
                    console.error('Unexpected error:', response.status);
                    return null;
                }
                return response.json();
            })
            .then(data => {
                if (data) {
                    console.log('Planner Data:', data);
                    populatePlannerModal(data); // 데이터를 모달에 채우기
                } else {
                    console.log('No data for selected date.');
                }
            })
            .catch(error => {
                console.error('Error fetching planner data:', error);
            });
    }

    function resetPlannerModal() {
        document.querySelectorAll('#goal-setting input[type="text"]').forEach(input => (input.value = ''));
        document.querySelectorAll('#goal-setting input[type="checkbox"]').forEach(checkbox => (checkbox.checked = false));
        document.getElementById('feedbackInput').value = '';
        document.querySelectorAll('.timestamp-cell').forEach(cell => (cell.style.backgroundColor = '#f9f9f9'));
    }

    function populatePlannerModal(data) {
        if (data.goals && data.goalCompletions) {
            const goalInputs = document.querySelectorAll('#goal-setting input[type="text"]');
            const goalCheckboxes = document.querySelectorAll('#goal-setting input[type="checkbox"]');
            goalInputs.forEach((input, index) => {
                input.value = data.goals[index] || '';
            });
            goalCheckboxes.forEach((checkbox, index) => {
                checkbox.checked = data.goalCompletions[index] || false;
            });
        }

        document.getElementById('feedbackInput').value = data.feedback || '';

        const timestamps = data.timestamps || [];
        const colors = data.timestampColors || [];
        const timestampCells = document.querySelectorAll('.timestamp-cell');
        timestampCells.forEach(cell => {
            const time = cell.dataset.time;
            const index = timestamps.indexOf(time);
            if (index !== -1) {
                cell.style.backgroundColor = colors[index];
            } else {
                cell.style.backgroundColor = '#f9f9f9';
            }
        });
    }

    // 플래너 저장
    function saveData() {
        const feedbackData = document.getElementById('feedbackInput')?.value.trim() || '';
        const goalData = [];
        const goalCompletions = [];

        // 목표 데이터 처리
        document.querySelectorAll('#goal-setting input[type="text"]').forEach((goalInput, index) => {
            if (goalInput.value.trim()) {
                goalData.push(goalInput.value.trim());
                const goalCheck = document.querySelector(`#GoalCheck${index + 1}`);
                goalCompletions.push(goalCheck?.checked || false);
            }
        });

        // 타임스탬프와 색상 데이터 처리
        const timestamps = [];
        const timestampColors = [];
        document.querySelectorAll('.timestamp-cell').forEach(cell => {
            const color = cell.style.backgroundColor;
            if (color && color !== 'rgb(249, 249, 249)') { // 기본 색상이 아닌 경우만 저장
                timestamps.push(cell.dataset.time);
                timestampColors.push(color);
            }
        });

        // 데이터 구성
        const data = {
            feedback: feedbackData,
            goals: goalData,
            goalCompletions: goalCompletions,
            timestamps: timestamps,
            timestampColors: timestampColors,
            date: selectedDate
        };

        // POST 요청으로 데이터 저장
        fetch('/api/planner', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                } else {
                    console.error('Error saving data:', data.error); // 에러 메시지 확인
                }
            })
            .catch(error => {
                console.error('Fetch error:', error);
            });
    }
});
