startUser();

function startUser(){
    loadUserHeader().then(r => {
        return readDataFromUrl();
    })
        .then(loggedUserId => {
            return Promise.all([
                readAllNotSentReimbursement(loggedUserId),
                readLoggedUserById(loggedUserId)
            ]);
        })
        .catch(error => {
            console.error(error);
        });
}

function readAllNotSentReimbursement(loggedUserId) {
    return new Promise((resolve, reject) => {
        fetch(`${REIMBURSEMENTS_API_URL}/false/${loggedUserId}`)
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
                        let textChangeBtn = `changeBtn${s.id}`;
                        newChangeButton.innerHTML = `<button id="${textChangeBtn}" class="pure-button">Change</button>`;
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
    let changeButton = `changeBtn${reimbursementId}`;

   if (checkButton === 'change'  && clickBtnID.startsWith('changeBtn')){
        window.location.href = `../creating/creating.html?user=${LOGGED_USER_ID}&reimbursement=${REIMBURSEMENT_ID}&createMode=`;
    }
}

changeReimbursementButtons.addEventListener("click",changeReimbursementButtonsPressed);

