startReimbursement();


function startReimbursement(){
    loadUserHeader().then(() => {
        return readDataFromUrl();
    })
        .then(loggedUserId => {
            readReimbursementById();
            return readLoggedUserById(loggedUserId);
        })
        .then(userGroupId => {
            return readUserGroupByIdToDetailsTable(userGroupId);
        })
        .catch(error => {
            console.error(error);
        });
}

function loadUserHeader() {
    return new Promise((resolve, reject) => {
        fetch('../common/user_header.html')
            .then(response => response.text())
            .then(data => {
                document.getElementById('header').innerHTML = data;
                resolve ();
            })
            .catch(error => console.error('Error:', error));
    });
}

function readReimbursementById() {
    fetch(`${REIMBURSEMENTS_API_URL}/${REIMBURSEMENT_ID}`)
        .then(response => response.json())
        .then((s) => {
            setReimbursementDetailsPlaceholders(s.name, s.startDate, s.endDate, s.distance, s.returnValue);
            calculations(s.startDate, s.endDate);
            s.userCosts.map(k => {
                makeUserCostRow(k.name, k.costValue, k.maxValue);
            });
        });
}

function setReimbursementDetailsPlaceholders(name, startDay, endDay, distance, returnValue){
    document.getElementById("reimbursement_name").textContent = name;
    document.getElementById("reimbursement_start_day").textContent = startDay;
    document.getElementById("reimbursement_end_day").textContent = endDay;
    document.getElementById("reimbursement_distance").textContent = distance;
    document.getElementById("reimbursement_value").textContent = returnValue;
}

function readUserGroupByIdToDetailsTable(loggedUserGroupId) {
    return new Promise(resolve => {
        fetch(`${USER_GROUPS_API_URL}/${loggedUserGroupId}`)
            .then(response => response.json())
            .then((s) => {
                let table = document.getElementById('user_group_params_table');
                let row = table.insertRow(-1);
                newCellInRow(row, 0, s.name);
                newCellInRow(row, 1, s.dailyAllowance);
                document.getElementById("user_group_daily_allowance").textContent = s.dailyAllowance;
                newCellInRow(row, 2, s.costPerKm);
                document.getElementById("user_group_cost_per_km").textContent = s.costPerKm;
                newCellInRow(row, 3, s.maxMileage);
                document.getElementById("user_group_max_mileage").textContent = s.maxMileage;
                newCellInRow(row, 4, s.maxRefund);
            });resolve();
    });
}

function calculations(startDate, endDate){
    let days;
    if (startDate !== null) {
        const data1 = startDate;
        const data2 = endDate;

        const parsedDate1 = new Date(data1);
        const parsedDate2 = new Date(data2);

        const differenceInTime = Math.abs(parsedDate2 - parsedDate1);

        days = Math.ceil(differenceInTime / (1000 * 60 * 60 * 24)) + 1;
    } else {
        days = 0;
    }
    document.getElementById("calculation_duration").textContent = days;
  }

function makeUserCostRow(id, name, maxValue){
    let table = document.getElementById('reimbursement_table_create');
    let row = table.insertRow(-1);
    newCellInRow(row, 0, id);
    newCellInRow(row, 1, name);
    newCellInRow(row, 2, maxValue);
}

