readAllUsers();

function readAllUsers() {
    fetch(`${USER_API_URL}`)
        .then((response) => response.json())
        .then((modulesPropArr) => {
            modulesPropArr.map(s => {
                let table = document.getElementById('users_table');
                let row = table.insertRow(-1);
                newCellInRow(row, 0, row.rowIndex);
                newCellInRow(row, 1, s.name);
                newCellInRow(row, 2, s.userGroup.name);

                let newCell = row.insertCell(3);
                const newButton = document.createElement("div");
                let textChangeBtn = `loginBtn${s.id}`;
                newButton.innerHTML = ` <button id="${textChangeBtn}" class="button-orange pure-button">Login</button>`;
                newCell.appendChild(newButton);
            });
        });
}

const linkButtons = document.getElementById('users_table');

const linkButtonsPressed = e => {
    const isButton = e.target.nodeName === 'BUTTON';
    if(!isButton){
        console.log("Not a button");
        return;}

    console.info(e);
    let clickBtnID = `${e.target.id}`;
    console.info(clickBtnID);
    LOGGED_USER_ID = clickBtnID.substring(8);
    let checkButton = clickBtnID.substring(0,5);

   if (checkButton === 'login'  && clickBtnID.startsWith('loginBtn')){
        window.location.href = `user/common/user.html?user=${LOGGED_USER_ID}`;
    }
}

linkButtons.addEventListener("click",linkButtonsPressed);
