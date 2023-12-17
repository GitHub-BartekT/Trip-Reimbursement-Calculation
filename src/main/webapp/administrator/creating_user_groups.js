readDataFromUrl();
readUserById();
setMode();

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
                    document.getElementById('information').innerHTML = `<h2>Adding a new User Group failed!</h2>`;
                });
            }
            const locationHeader = response.headers.get('Location');
            if (locationHeader) {
                const urlParts = locationHeader.split('/');
                USER_GROUP_ID = urlParts[urlParts.length - 1];
                pageChangingModeUserGroup();
            }
            document.getElementById('information').innerHTML = `<h2>You added a new User Group!</h2>`;
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
                document.getElementById('information').innerHTML = `<h2>You changed the User Group!</h2>`;
            } else {
                document.getElementById('information').innerHTML = `<h2>Changing was failed!</h2>`;
            }
        })
        .catch(console.warn);
}

//deleteUserGroup
function doDeleteUserGroupInCreatingMode() {
    fetch(`${USER_GROUPS_API_URL}/${USER_GROUP_ID}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                pageCreatingModeUserGroup();
                document.getElementById('information').innerHTML = `<h2>You deleted the User Group!</h2>`;
            } else {
                document.getElementById('information').innerHTML = `<h2>Deleting was failed!</h2>`;
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
