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

function readUserGroupByIdToTable(loggedUserGroupId) {
    return new Promise(resolve => {
        fetch(`${USER_GROUPS_API_URL}/${loggedUserGroupId}`)
            .then(response => response.json())
            .then((s) => {
                let table = document.getElementById('user_group_params_table');
                let row = table.insertRow(-1);
                newCellInRow(row, 0, s.name);
                newCellInRow(row, 1, s.dailyAllowance);
                newCellInRow(row, 2, s.costPerKm);
                newCellInRow(row, 3, s.maxMileage);
                newCellInRow(row, 4, s.maxRefund);

            });resolve();
    });
}

function readOptionalReimbursement(booleanFlat, loggedUserId) {
    return new Promise((resolve, reject) => {
        fetch(`${REIMBURSEMENTS_API_URL}/${booleanFlat}/${loggedUserId}`)
            .then((response) => response.json())
            .then((modulesPropArr) => {
                let table = document.getElementById('reimbursement_table');

                if (modulesPropArr.length === 0) {
                    let row = table.insertRow(-1);
                    let cell = row.insertCell(0);
                    cell.colSpan = 4;
                    cell.textContent = "No reimbursement to sent for approve"
                } else {
                    modulesPropArr.forEach(s => {
                        let row = table.insertRow(-1);
                        newCellInRow(row, 0, s.id);
                        newCellInRow(row, 1, s.name);
                        newCellInRow(row, 2, s.returnValue);

                        let newChangeCell = row.insertCell(3);
                        const newChangeButton = document.createElement("div");

                        if (booleanFlat) {
                            let textChangeBtn = `detailBtn${s.id}`;
                            newChangeButton.innerHTML = `<button id="${textChangeBtn}" class="pure-button">Details</button>`;
                        } else {
                            let textChangeBtn = `changeBtn${s.id}`;
                            newChangeButton.innerHTML = `<button id="${textChangeBtn}" class="pure-button">Change</button>`;

                        }
                        newChangeCell.appendChild(newChangeButton);
                    });
                }
                resolve ();
            });
    });
}

const changeReimbursementButtons = document.getElementById('reimbursement_table');

const changeReimbursementButtonsPressed = e => {
    const isButton = e.target.nodeName === 'BUTTON';
    if(!isButton){
        console.log("Not a button");
        return;}

    console.info(e);
    let clickBtnID = `${e.target.id}`;
    console.info(clickBtnID);
    let reimbursementId = clickBtnID.substring(9);
    REIMBURSEMENT_ID = reimbursementId;
    let checkButton = clickBtnID.substring(0,6);

    if (checkButton === 'change'  && clickBtnID.startsWith('changeBtn')){
        window.location.href = `../creating/creating.html?user=${LOGGED_USER_ID}&reimbursement=${REIMBURSEMENT_ID}&createMode=`;
    } else {
        window.location.href = `../history/reimbursement_details.html?user=${LOGGED_USER_ID}&reimbursement=${REIMBURSEMENT_ID}`;
    }
}

changeReimbursementButtons.addEventListener("click",changeReimbursementButtonsPressed);