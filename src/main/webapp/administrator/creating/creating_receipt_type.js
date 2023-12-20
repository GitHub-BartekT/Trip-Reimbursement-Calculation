readDataFromUrl();
readUserById();
setMode();
getUserGroups();
readReceiptTypeData();

function readReceiptTypeData(){
    if (!CREATE_MODE) {
        readReceiptTypeById();
        getReceiptUserGroups();
    }
}

function readReceiptTypeById() {
    fetch(`${RECEIPT_TYPE_API_URL}/${RECEIPT_TYPE_ID}`)
        .then(response => response.json())
        .then((s) => {
            setReceiptTypePlaceholders(s.name, s.maxValue);
        });
}

function makeReceiptType(){
    let receipt_type_name = document.getElementById(`receipt_type_name`).value;
    let receipt_type_max_value = document.getElementById(`receipt_type_max_value`).value;

    if (CREATE_MODE) {
        doPostReceiptType(receipt_type_name, receipt_type_max_value);
    } else {
        doPutReceiptType(receipt_type_name, receipt_type_max_value);
    }
}

function doPostReceiptType(receipt_type_name, receipt_type_max_value){
    let bodyAddReceiptType = {
        name: receipt_type_name,
        maxValue:  receipt_type_max_value
    };

    fetch(`${RECEIPT_TYPE_API_URL}?integerList=`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(bodyAddReceiptType)
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(error => {
                    document.getElementById('information').innerText = `Adding a new Receipt Type failed!`;
                });
            }
            const locationHeader = response.headers.get('Location');
            if (locationHeader) {
                const urlParts = locationHeader.split('/');
                RECEIPT_TYPE_ID = urlParts[urlParts.length - 1];
                pageChangingModeReceiptType();
            }
            document.getElementById('information').innerText = `You added a new Receipt Type!`;
        })
        .catch(console.warn);
}

function doPutReceiptType(receipt_type_name, receipt_type_max_value){
    let bodyAddReceiptType = {
        name: receipt_type_name,
        maxValue:  receipt_type_max_value,
        id: RECEIPT_TYPE_ID
    };

    fetch(`${RECEIPT_TYPE_API_URL}`, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(bodyAddReceiptType)
    })
        .then(response => {
            if (response.ok) {
                document.getElementById('information').innerText = `You changed the Receipt Type!`;
            } else {
                document.getElementById('information').innerText = `Changing was failed!`;
            }
        })
        .catch(console.warn);
}

function getUserGroups() {
    fetch(`${USER_GROUPS_API_URL}`)
        .then((response) => response.json())
        .then((userGroupArr) => {
            const list = userGroupArr.map(s =>
                `<option value="${s.id}">User Group: ${s.name}</option>`)
                .join('\n');
            document.getElementById('user_groups_list').innerHTML = list;
        });
}

function getUserGroup() {
    return document.getElementById('user_groups_list').value;
}

function getReceiptUserGroups(){
    fetch(`${USER_GROUPS_API_URL}/receiptType/${RECEIPT_TYPE_ID}`)
        .then((response) => response.json())
        .then((receiptArr) => {
            receiptArr.map(s => {
                makeUserGroupRow(s.id, s.name);
            });
        });
}

function makeUserGroupRow(id, name){
    let table = document.getElementById('receipt_type_table_create');
    let row = table.insertRow(-1);
    newCellInRow(row, 0, id);
    newCellInRow(row, 1, name);

    let newChangeCell = row.insertCell(2);
    const newDeleteButton = document.createElement("div");
    let textDeleteBtn = `deleteBtn${id}`;
    newDeleteButton.innerHTML = `<button id="${textDeleteBtn}" class="button-error pure-button">Delete</button>`;
    newChangeCell.appendChild(newDeleteButton);
}

function doAssignUserGroupToReceiptType(){
    fetch(`${RECEIPT_TYPE_API_URL}/add/${getUserGroup()}`, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(RECEIPT_TYPE_ID)
    })
        .then(response => {
            if (response.ok) {
                document.getElementById('information').innerText = `You assign User Group to the Receipt Type!`;
            } else {
                document.getElementById('information').innerText = `Assign was failed!`;
            }
        })
        .then(() => {
            deleteRows(6, 'receipt_type_table_create');
        })
        .then(() => {
            getReceiptUserGroups();
        })
        .catch(console.warn);
}

function doDeleteReceiptType() {
    fetch(`${RECEIPT_TYPE_API_URL}/${RECEIPT_TYPE_ID}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                pageCreatingModeReceiptType();
                document.getElementById('information').innerText = `You deleted the Receipt Type!`;
            } else {
                document.getElementById('information').innerHTML = `Deleting was failed!`;
            }
        })
        .catch(console.warn);
}

function setMode(){
    if (CREATE_MODE){
        pageCreatingModeReceiptType();
    } else {
        pageChangingModeReceiptType();
    }
}

function pageCreatingModeReceiptType(){
    CREATE_MODE = true;
    const topTextContainer = document.getElementById('top-text-container');
    topTextContainer.innerHTML = `<h2>Your new Receipt Type:</h2>`;
    document.getElementById("accept_btn").innerText = "Add Receipt Type";
    changeBtnToDisable("add_user_group_btn");
    changeBtnToDisableDelete("delete_btn");
    setReceiptTypePlaceholders("Receipt Type Name", 0);
    deleteRows(6,'receipt_type_table_create');

}

function pageChangingModeReceiptType(){
    CREATE_MODE = false;
    const topTextContainer = document.getElementById('top-text-container');
    topTextContainer.innerHTML = `<h2>Modified receipt type id:${RECEIPT_TYPE_ID}</h2>`;
    document.getElementById("accept_btn").innerText = "Save changes";
    changeBtnToPrimary("add_user_group_btn");
    changeBtnToDelete("delete_btn");
}

function setReceiptTypePlaceholders(name, maxValue){
    if (CREATE_MODE){
        document.getElementById("receipt_type_name").setAttribute("placeholder", name);
        document.getElementById("receipt_type_name").value = "";
    } else {
        changePlaceholderAndValue("receipt_type_name", name);
    }
    changePlaceholderAndValue("receipt_type_max_value", maxValue);

}