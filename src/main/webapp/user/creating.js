readDataFromUrl();
readUserById();

function makeReimbursement(){
    let reimb_name = document.getElementById(`reimbursement_name`).value;
    let reimb_startDate = document.getElementById(`reimbursement_start_day`).value;
    let reimb_endDate= document.getElementById(`reimbursement_end_day`).value;
    let reimb_distance= document.getElementById(`reimbursement_distance`).value;

    if (isCreatingMode()) {
        doPostReimbursement(reimb_name, reimb_startDate, reimb_endDate, reimb_distance);
    } else{

    }
}

function doPostReimbursement(reimb_name, reimb_startDate, reimb_endDate, reimb_distance){
    let bodyAddReimbursement = {
        name: reimb_name,
        startDate:  reimb_startDate,
        endDate: reimb_endDate,
        distance: reimb_distance,
        userId: USER_ID
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
                    document.getElementById('information').innerHTML = `<h2>Adding a new reimbursement failed!</h2>`;
                });
            }
            const locationHeader = response.headers.get('Location');
            if (locationHeader) {
                const urlParts = locationHeader.split('/');
                REIMBURSEMENT_ID = urlParts[urlParts.length - 1];
                pageChangingMode()
            }
            document.getElementById('information').innerHTML = `<h2>You added a new reimbursements!</h2>`;
        })
        .catch(console.warn);
}

function doDeleteReimbursement() {
    fetch(`${REIMBURSEMENTS_API_URL}/${REIMBURSEMENT_ID}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                pageCreatingMode();
                document.getElementById('information').innerHTML = `<h2>You deleted the reimbursement!</h2>`;
                REIMBURSEMENT_ID = 0;
            } else {
                document.getElementById('information').innerHTML = `<h2>Deleting was failed!</h2>`;
            }
        })
        .catch(console.warn);
}

function pageCreatingMode(){
    const topTextContainer = document.getElementById('top-text-container');
    topTextContainer.innerHTML = `<h2>Your new reimbursement:</h2>`;
    document.getElementById("accept_btn").innerText = "Add Reimbursement";
    changeBtnToDisable("add_cost_btn");
    changeBtnToDisableDelete("delete_btn");
}

function pageChangingMode(){
    const topTextContainer = document.getElementById('top-text-container');
    topTextContainer.innerHTML = `<h2>Your reimbursement id:${REIMBURSEMENT_ID}</h2>`;
    document.getElementById("accept_btn").innerText = "Save changes";
    changeBtnToPrimary("add_cost_btn");
    changeBtnToDelete("delete_btn");
}

function isCreatingMode() {
    return REIMBURSEMENT_ID <= 0;
}

function changeBtnToPrimary(btnId){
    document.getElementById(btnId).className = "";
    document.getElementById(btnId).classList.add("pure-button", "pure-button-primary");
}

function changeBtnToDisable(btnId){
    document.getElementById(btnId).className = "";
    document.getElementById(btnId).classList.add("pure-button", "pure-button-disabled");
}

function changeBtnToDelete(btnId){
    document.getElementById(btnId).className = "";
    document.getElementById(btnId).classList.add("button-error", "pure-button");
}

function changeBtnToDisableDelete(btnId){
    document.getElementById(btnId).className = "";
    document.getElementById(btnId).classList.add("button-error", "pure-button", "pure-button-disabled");
}