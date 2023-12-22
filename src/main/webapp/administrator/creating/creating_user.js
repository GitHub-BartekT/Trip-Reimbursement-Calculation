startCreatingUser();
getUserGroups();

function startCreatingUser(){
    loadHeader().then(r => {
        return readDataFromUrl();
    })
        .then(loggedUserId => {
            if (CREATE_MODE){
                pageCreatingModeUser();
            } else {
                pageChangingModeUser();
                readUserDataById();
            }
            return readLoggedUserById(loggedUserId);
        })
        .catch(error => {
            console.error(error);
        });
}

function readUserDataById() {
    fetch(`${USER_API_URL}/${USER_ID}`)
        .then(response => response.json())
        .then((s) => {
            setUserPlaceholders(s.name, `User Group: ${s.userGroup.name}`);
        });
}

function makeUser(){
    let user_name = document.getElementById(`user_name`).value;
    if (CREATE_MODE) {
        doPostUserGroup(user_name);
    } else {
        doPutUserGroup(user_name);
    }
}

function doPostUserGroup(user_name){
    let bodyAddUserGroup = {
        name: user_name,
        userGroupId: USER_GROUP_ID
    };

    fetch(`${USER_API_URL}/create`, {
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
                    document.getElementById('information').innerText = `Adding a new User failed!`;
                });
            }
            const locationHeader = response.headers.get('Location');
            if (locationHeader) {
                const urlParts = locationHeader.split('/');
                USER_ID = urlParts[urlParts.length - 1];
                pageChangingModeUserGroup();
            }
            document.getElementById('information').innerText = `You added a new User!`;
        })
        .catch(console.warn);
}

function doPutUserGroup(user_name){
    let bodyAddUserGroup = {
        id: USER_ID,
        name: user_name,
        userGroupId: USER_GROUP_ID
    };

    fetch(`${USER_API_URL}/update`, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(bodyAddUserGroup)
    })
        .then(response => {
            if (response.ok) {
                document.getElementById('information').innerText = `You changed the User!`;
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
    var select = document.getElementById('user_groups_list');
    var selectedOption = select.options[select.selectedIndex];

    if (selectedOption) {
        document.getElementById('user_group').innerText = selectedOption.innerText;
        USER_GROUP_ID = selectedOption.value;
        return selectedOption.innerText;
    } else {
        select.selectedIndex = 0;
        var firstOption = select.options[0];
        document.getElementById('user_group').innerText = firstOption.innerText;
        USER_GROUP_ID = firstOption.value;
        return firstOption.value;
    }
}

function doDeleteUserGroup() {
    fetch(`${USER_API_URL}/${USER_ID}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                pageCreatingModeUserGroup();
                document.getElementById('information').innerText = `You deleted the User!`;
            } else {
                document.getElementById('information').innerText = `Deleting was failed!`;
            }
        })
        .catch(console.warn);
}

function pageCreatingModeUser(){
    CREATE_MODE = true;
    const topTextContainer = document.getElementById('top-text-container');
    topTextContainer.innerHTML = `<h2>Your new User:</h2>`;
    document.getElementById("accept_btn").innerText = "Add User";
    changeBtnToDisableDelete("delete_btn");
    setUserPlaceholders("User Name", getUserGroup());
}

function pageChangingModeUser(){
    CREATE_MODE = false;
    const topTextContainer = document.getElementById('top-text-container');
    topTextContainer.innerHTML = `<h2>Modified user settings:${USER_ID}</h2>`;
    document.getElementById("accept_btn").innerText = "Save changes";
    changeBtnToDelete("delete_btn");
}

function setUserPlaceholders(name, userGroup) {
    if (CREATE_MODE){
        document.getElementById("user_name").setAttribute("placeholder", "User Name");
        document.getElementById("user_name").value = "";
    } else {
        changePlaceholderAndValue("user_name", name);
    }
    document.getElementById("user_group").innerText = userGroup;
}