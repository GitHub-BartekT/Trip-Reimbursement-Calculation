start();
setMode();

function start(){
    readDataFromUrl()
        .then(loggedUserId => {
            return readLoggedUserByIdReturnUserGroupId(loggedUserId);
        })
        .then(userGroupId => {
            return getUserCosts(userGroupId);
        })
        .catch(error => {
            console.error(error);
        });
}

function makeReimbursement(){
    let reimbursement_name = document.getElementById(`reimbursement_name`).value;
    let reimbursement_startDate = document.getElementById(`reimbursement_start_day`).value;
    let reimbursement_endDate= document.getElementById(`reimbursement_end_day`).value;
    let reimbursement_distance= document.getElementById(`reimbursement_distance`).value;

    if (CREATE_MODE) {
        doPostReimbursement(reimbursement_name, reimbursement_startDate, reimbursement_endDate, reimbursement_distance);
    } else {
        doPutReimbursement(reimbursement_name, reimbursement_startDate, reimbursement_endDate, reimbursement_distance);
    }
}

function doPostReimbursement(reimbursement_name, reimbursement_startDate, reimbursement_endDate, reimbursement_distance){
    let bodyAddReimbursement = {
        name: reimbursement_name,
        startDate:  reimbursement_startDate,
        endDate: reimbursement_endDate,
        distance: reimbursement_distance,
        userId: LOGGED_USER_ID
    };

    fetch(`${REIMBURSEMENTS_API_URL}`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(bodyAddReimbursement)
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(error => {
                    document.getElementById('user_information').innerText = `Adding a new reimbursement failed!`;
                });
            }
            const locationHeader = response.headers.get('Location');
            if (locationHeader) {
                const urlParts = locationHeader.split('/');
                REIMBURSEMENT_ID = urlParts[urlParts.length - 1];
                pageChangingMode();
            }
            document.getElementById('user_information').innerText = `You added a new reimbursements!`;
        })
        .catch(console.warn);
}

function doPutReimbursement(reimb_name, reimb_startDate, reimb_endDate, reimb_distance){
    let bodyAddReimbursement = {
        id: REIMBURSEMENT_ID,
        name: reimb_name,
        startDate:  reimb_startDate,
        endDate: reimb_endDate,
        distance: reimb_distance,
        userId: LOGGED_USER_ID
    };

    fetch(`${REIMBURSEMENTS_API_URL}`, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(bodyAddReimbursement)
    })
        .then(response => {
            if (response.ok) {
                document.getElementById('user_information').innerText = `You changed the reimbursement!`;
            } else {
                document.getElementById('user_information').innerText = `Changing was failed!`;
            }
        })
        .catch(console.warn);
}

function getUserCosts(userGroupId) {
    return new Promise((resolve, reject) => {
        fetch(`${RECEIPT_TYPE_API_URL}/userGroup/${userGroupId}`)
            .then((response) => response.json())
            .then((userGroupArr) => {
                const list = userGroupArr.map(s =>
                    `<option value="${s.id}">User Group: ${s.name}</option>`)
                    .join('\n');
                document.getElementById('receipt_list').innerHTML = list;
                resolve();
            });
    });
}

function doDeleteReimbursement() {
    fetch(`${REIMBURSEMENTS_API_URL}/${REIMBURSEMENT_ID}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                pageCreatingMode();
                document.getElementById('user_information').innerText = `You deleted the reimbursement!`;
            } else {
                document.getElementById('information').innerText = `Deleting was failed!`;
            }
        })
        .catch(console.warn);
}

function setMode(){
    if (CREATE_MODE){
        pageCreatingMode();
    } else {
        pageChangingMode();
        //readUserDataById();
    }
}

function pageCreatingMode(){
    CREATE_MODE = true;
    const topTextContainer = document.getElementById('top-text-container');
    topTextContainer.innerHTML = `<h2>Your new reimbursement:</h2>`;
    document.getElementById("accept_btn").innerText = "Add Reimbursement";
    changeBtnToDisable("add_cost_btn");
    changeBtnToDisableDelete("delete_btn");
}

function pageChangingMode(){
    CREATE_MODE = false;
    const topTextContainer = document.getElementById('top-text-container');
    topTextContainer.innerHTML = `<h2>Your reimbursement id:${REIMBURSEMENT_ID}</h2>`;
    document.getElementById("accept_btn").innerText = "Save changes";
    changeBtnToPrimary("add_cost_btn");
    changeBtnToDelete("delete_btn");
}