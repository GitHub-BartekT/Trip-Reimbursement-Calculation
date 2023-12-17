const USER_GROUPS_API_URL = 'http://localhost:8080/groups';
const RECEIPT_TYPE_API_URL ='http://localhost:8080/receipts';
const USER_API_URL = 'http://localhost:8080/users';
const REIMBURSEMENTS_API_URL = 'http://localhost:8080/reimbursements';
let USER_ID;
let USER_NAME;
let USER_GROUP_ID;
let USER_GROUP_NAME;
let REIMBURSEMENT_ID;
let RECEIPT_TYPE_ID;
let CREATE_MODE = Boolean(true);

//read data from url
function readDataFromUrl() {
    const queryString = window.location.search;
    console.log(queryString);
    const urlParams = new URLSearchParams(queryString);
    USER_ID = urlParams.get('user');
    REIMBURSEMENT_ID = urlParams.get('reimbursement');
    CREATE_MODE = urlParams.get('createMode');
}

//readUserGroups
function readAllUserGroups() {
    fetch(`${USER_GROUPS_API_URL}`)
        .then((response) => response.json())
        .then((modulesPropArr) => {
            modulesPropArr.map(s => {
                let table = document.getElementById('groups_table');
                let row = table.insertRow(-1);
                newCellInRow(row, 0, s.id);
                newCellInRow(row, 1, s.name);
                let cells = row.cells;
            });
        });
}

function readAllUserGroupsWithDetails() {
    fetch(`${USER_GROUPS_API_URL}`)
        .then((response) => response.json())
        .then((modulesPropArr) => {
            modulesPropArr.map(s => {
                let table = document.getElementById('groups_table');
                let row = table.insertRow(-1);
                newCellInRow(row, 0, s.id);
                newCellInRow(row, 1, s.name);
                newCellInRow(row, 2, s.dailyAllowance);
                newCellInRow(row, 3, s.costPerKm);
                newCellInRow(row, 4, s.maxMileage);
                newCellInRow(row, 5, s.maxRefund);
                newCellInRow(row, 6, s.numberOfUsers);
                let cells = row.cells;
            });
        });
}

//readUserById
function readUserById() {
    fetch(`${USER_API_URL}/${USER_ID}`)
        .then(response => response.json())
        .then((s) => {
            USER_NAME = s.name;
            USER_GROUP_ID = s.userGroup.id;
            USER_GROUP_NAME = s.userGroup.name;
            let text = `User ID: ${USER_NAME}, User Group: ${USER_GROUP_NAME}`;
            document.getElementById("user_info").innerHTML = `<a>${text}</a>`;
        });
}

//readReimbursements
function readAllReimbursement() {
    fetch(`${REIMBURSEMENTS_API_URL}`)
        .then((response) => response.json())
        .then((modulesPropArr) => {
            modulesPropArr.map(s => {
                let table = document.getElementById('reimbursement_table');
                let row = table.insertRow(-1);

                newCellInRow(row, 0, s.id);
                newCellInRow(row, 1, s.name);
                newCellInRow(row, 2, s.returnValue);
                let cells = row.cells;
            });
        });
}

function newCellInRow(row, int, text){
    let newCell = row.insertCell(int);
    let newText = document.createTextNode(text);
    newCell.appendChild(newText);
}

function processOkResponse(response = {}) {
    if (response.ok) {
        return response.json();
    }
    throw new Error(`Status not 200 (${response.status})`);
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