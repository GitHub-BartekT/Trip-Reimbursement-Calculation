readDataFromUrl();
readLoggedUserByIdReturnUserGroupId();
readAllUserGroupsWithDetails();

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

                let newCell = row.insertCell(7);
                const newButton = document.createElement("div");
                let textChangeBtn = `changeBtn${s.id}`;
                newButton.innerHTML = ` <button id="${textChangeBtn}" class="pure-button">Change</button>`;
                newCell.appendChild(newButton);

                let newChangeCell = row.insertCell(8);
                const newChangeButton = document.createElement("div");
                let textDeleteBtn = `deleteBtn${s.id}`;
                newChangeButton.innerHTML = `<button id="${textDeleteBtn}" class="button-error pure-button">Delete</button>`;
                newChangeCell.appendChild(newChangeButton);
            });
        });
}

const changeModuleButtons = document.getElementById('groups_table');

const changeUserGroupButtonsPressed = e => {
    const isButton = e.target.nodeName === 'BUTTON';
    if(!isButton){
        console.log("Not a button");
        return;}

    console.info(e);
    let clickBtnID = `${e.target.id}`;
    console.info(clickBtnID);
    let userGroupId = clickBtnID.substring(9);
    USER_GROUP_ID = userGroupId;
    let checkButton = clickBtnID.substring(0,6);
    let changeButton = `changeBtn${userGroupId}`;
    let deleteButton = `deleteBtn${userGroupId}`;

    // Click "Delete" button
    if (checkButton === 'delete'){
        fetch(`${USER_GROUPS_API_URL}/${USER_GROUP_ID}`, {
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
        window.location.href = `../creating/creating_user_groups.html?user=${LOGGED_USER_ID}&group=${USER_GROUP_ID}&createMode=`;
    }
}

changeModuleButtons.addEventListener("click",changeUserGroupButtonsPressed);

function createNewUserGroupLink(){
    window.location.href = `../creating/creating_user_groups.html?user=${LOGGED_USER_ID}&createMode=Yes`;
}