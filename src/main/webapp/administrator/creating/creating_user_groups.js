readDataFromUrl();
readUserById();
setMode();
getReceipts();
getUserGroupReceipts();
readUserGroupById();

function readUserGroupById() {
    fetch(`${USER_GROUPS_API_URL}/${USER_GROUP_ID}`)
        .then(response => response.json())
        .then((s) => {
            changePlaceholderAndValue("user_group_name", `${s.name}`);
            changePlaceholderAndValue("user_group_daily_allowance", `${s.dailyAllowance}`);
            changePlaceholderAndValue("user_group_cost_per_km", `${s.costPerKm}`);
            changePlaceholderAndValue("user_group_max_mileage", `${s.maxMileage}`);
            changePlaceholderAndValue("user_group_max_refund", `${s.maxRefund}`);
        });
}

function makeUserGroup(){
    let user_group_name = document.getElementById(`user_group_name`).value;
    let user_group_daily_allowance = document.getElementById(`user_group_daily_allowance`).value;
    let user_group_cost_per_km= document.getElementById(`user_group_cost_per_km`).value;
    let user_group_max_mileage= document.getElementById(`user_group_max_mileage`).value;
    let user_group_max_refund= document.getElementById(`user_group_max_refund`).value;

    if (CREATE_MODE) {
        doPostUserGroup(user_group_name, user_group_daily_allowance, user_group_cost_per_km,
            user_group_max_mileage, user_group_max_refund);
    } else {
        doPutUserGroup(user_group_name, user_group_daily_allowance, user_group_cost_per_km,
             user_group_max_mileage, user_group_max_refund);
    }
}

function doPostUserGroup(user_group_name, user_group_daily_allowance, user_group_cost_per_km,
                         user_group_max_mileage, user_group_max_refund){
    let bodyAddUserGroup = {
        name: user_group_name,
        dailyAllowance:  user_group_daily_allowance,
        costPerKm: user_group_cost_per_km,
        maxMileage: user_group_max_mileage,
        maxRefund: user_group_max_refund
    };

    fetch(`${USER_GROUPS_API_URL}`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(bodyAddUserGroup)
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(error => {
                    document.getElementById('information').innerText = `Adding a new User Group failed!`;
                });
            }
            const locationHeader = response.headers.get('Location');
            if (locationHeader) {
                const urlParts = locationHeader.split('/');
                USER_GROUP_ID = urlParts[urlParts.length - 1];
                pageChangingModeUserGroup();
            }
            document.getElementById('information').innerText = `You added a new User Group!`;
        })
        .catch(console.warn);
}

function doPutUserGroup(user_group_name, user_group_daily_allowance, user_group_cost_per_km,
                        user_group_max_mileage, user_group_max_refund){
    let bodyAddUserGroup = {
        id: USER_GROUP_ID,
        name: user_group_name,
        dailyAllowance:  user_group_daily_allowance,
        costPerKm: user_group_cost_per_km,
        maxMileage: user_group_max_mileage,
        maxRefund: user_group_max_refund
    };

    fetch(`${USER_GROUPS_API_URL}`, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(bodyAddUserGroup)
    })
        .then(response => {
            if (response.ok) {
                document.getElementById('information').innerText = `You changed the User Group!`;
            } else {
                document.getElementById('information').innerText = `Changing was failed!`;
            }
        })
        .catch(console.warn);
}

function getReceipts() {
    fetch(`${RECEIPT_TYPE_API_URL}`)
        .then((response) => response.json())
        .then((receiptArr) => {
            const list = receiptArr.map(s =>
                `<option value="${s.id}">${s.name}, max Value = ${s.maxValue}</option>`)
                .join('\n');
            document.getElementById('receipt_list').innerHTML = list;
        });
}

function getReceipt(){
    return document.getElementById('receipt_list').value;
}

function getUserGroupReceipts(){
    fetch(`${RECEIPT_TYPE_API_URL}/userGroup/${USER_GROUP_ID}`)
        .then((response) => response.json())
        .then((receiptArr) => {
            receiptArr.map(s => {
                makeReceiptRow(s.id, s.name, s.maxValue);
            });
        });
}

function makeReceiptRow(id, name, maxValue){
    let table = document.getElementById('user_group_table_create');
    let row = table.insertRow(-1);
    newCellInRow(row, 0, id);
    let text = `${name}, max Value = ${maxValue}`;
    newCellInRow(row, 1, text);

    let newChangeCell = row.insertCell(2);
    const newDeleteButton = document.createElement("div");
    let textDeleteBtn = `deleteBtn${id}`;
    newDeleteButton.innerHTML = `<button id="${textDeleteBtn}" class="button-error pure-button">Delete</button>`;
    newChangeCell.appendChild(newDeleteButton);
}

function doAssignReceiptTypeToUserGroup(){
    fetch(`${USER_GROUPS_API_URL}/add/${getReceipt()}`, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(USER_GROUP_ID)
    })
        .then(response => {
            if (response.ok) {
                document.getElementById('information').innerText = `You assign Receipt Type the User Group!`;
            } else {
                document.getElementById('information').innerText = `Assign was failed!`;
            }
        })
        .then(() => {
            deleteReceiptRows(9, 'user_group_table_create');
        })
        .then(() => {
            return getUserGroupReceipts();
        })
        .catch(console.warn);
}

const deleteReceiptButtons = document.getElementById('user_group_table_create');

const deleteReceiptTypeButtonsPressed = e => {
    const isButton = e.target.nodeName === 'BUTTON';
    if(!isButton){
        console.log("Not a button");
        return;}

    console.info(e);
    let clickBtnID = `${e.target.id}`;
    console.info(clickBtnID);
    let receiptTypeId = clickBtnID.substring(9);
    RECEIPT_TYPE_ID = receiptTypeId;
    let checkButton = clickBtnID.substring(0,6);
    let deleteButton = `deleteBtn${receiptTypeId}`;

    // Click "Delete" button
    if (checkButton === 'delete') {
        console.info(getReceipt());
        fetch(`${USER_GROUPS_API_URL}/remove/${RECEIPT_TYPE_ID}`, {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(USER_GROUP_ID)
        })
            .then(response => {
                if (response.ok) {
                    changeBtnToDisableDelete(deleteButton);
                    document.getElementById('information').innerText = `You deleted Receipt Type from User Group!`;
                } else {
                    document.getElementById('information').innerText = `Deleting was failed!`;
                }
            })
            .catch(console.warn);
    }
}

deleteReceiptButtons.addEventListener("click",deleteReceiptTypeButtonsPressed);

function doDeleteUserGroup() {
    fetch(`${USER_GROUPS_API_URL}/${USER_GROUP_ID}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                pageCreatingModeUserGroup();
                document.getElementById('information').innerText = `You deleted the User Group!`;
            } else {
                document.getElementById('information').innerText = `Deleting was failed!`;
            }
        })
        .catch(console.warn);
}

function setMode(){
    if (CREATE_MODE){
        pageCreatingModeUserGroup();
    } else {
        pageChangingModeUserGroup();
    }
}

function pageCreatingModeUserGroup(){
    CREATE_MODE = true;
    const topTextContainer = document.getElementById('top-text-container');
    topTextContainer.innerHTML = `<h2>Your new User Group:</h2>`;
    document.getElementById("accept_btn").innerText = "Add User Group";
    changeBtnToDisable("add_cost_btn");
    changeBtnToDisableDelete("delete_btn");
}

function pageChangingModeUserGroup(){
    CREATE_MODE = false;
    const topTextContainer = document.getElementById('top-text-container');
    topTextContainer.innerHTML = `<h2>Modified user group id:${USER_GROUP_ID}</h2>`;
    document.getElementById("accept_btn").innerText = "Save changes";
    changeBtnToPrimary("add_cost_btn");
    changeBtnToDelete("delete_btn");
}
