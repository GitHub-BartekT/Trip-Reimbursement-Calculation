const USER_API_URL = 'http://localhost:8080/users';
const REIMBURSEMENTS_API_URL = 'http://localhost:8080/reimbursements';
let USER_ID;
let USER_NAME;
let USER_GROUP_ID;
let USER_GROUP_NAME;
let REIMBURSEMENT_ID;

//read data from url
function readDataFromUrl() {
    const queryString = window.location.search;
    console.log(queryString);
    const urlParams = new URLSearchParams(queryString);
    USER_ID = urlParams.get('user');
    REIMBURSEMENT_ID = urlParams.get('reimbursement');
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