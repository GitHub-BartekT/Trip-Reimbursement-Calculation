startAdministrator()
readAllReceiptTypesWithDetails();

function readAllReceiptTypesWithDetails() {
    fetch(`${RECEIPT_TYPE_API_URL}`)
        .then((response) => response.json())
        .then((modulesPropArr) => {
            modulesPropArr.map(s => {
                let table = document.getElementById('groups_table');
                let row = table.insertRow(-1);
                newCellInRow(row, 0, s.id);
                newCellInRow(row, 1, s.name);
                newCellInRow(row, 2, s.maxValue);

                let newCell = row.insertCell(3);
                const newButton = document.createElement("div");
                let textChangeBtn = `changeBtn${s.id}`;
                newButton.innerHTML = ` <button id="${textChangeBtn}" class="pure-button">Change</button>`;
                newCell.appendChild(newButton);

                let newChangeCell = row.insertCell(4);
                const newChangeButton = document.createElement("div");
                let textDeleteBtn = `deleteBtn${s.id}`;
                newChangeButton.innerHTML = `<button id="${textDeleteBtn}" class="button-error pure-button">Delete</button>`;
                newChangeCell.appendChild(newChangeButton);
            });
        });
}

const changeModuleButtons = document.getElementById('groups_table');

const changeReceiptTypeButtonsPressed = e => {
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
    let changeButton = `changeBtn${receiptTypeId}`;
    let deleteButton = `deleteBtn${receiptTypeId}`;

    // Click "Delete" button
    if (checkButton === 'delete'){
        fetch(`${RECEIPT_TYPE_API_URL}/${RECEIPT_TYPE_ID}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    changeBtnToDisable(changeButton);
                    changeBtnToDisableDelete(deleteButton);
                }
            })
            .catch(console.warn);
    } else if (checkButton === 'change'  && clickBtnID.startsWith('changeBtn')){
        window.location.href = `../creating/creating_receipt_type.html?user=${LOGGED_USER_ID}&receipt=${RECEIPT_TYPE_ID}&createMode=`;
    }
}

changeModuleButtons.addEventListener("click",changeReceiptTypeButtonsPressed);

function createNewReceiptTypeLink(){
    window.location.href = `../creating/creating_receipt_type.html?user=${LOGGED_USER_ID}&createMode=Yes`;
}