const USER_API_URL = 'http://localhost:8080/users';
let USER_NAME;
let USER_GROUP_ID;
let USER_GROUP_NAME;

const queryString = window.location.search;
console.log(queryString);
const urlParams = new URLSearchParams(queryString);
const USER_ID = urlParams.get('user');

//readUserById
fetch(`${USER_API_URL}/${USER_ID}`)
    .then(response => response.json())
    .then((s) => {
        USER_NAME = s.name;
        USER_GROUP_ID = s.userGroup.id;
        USER_GROUP_NAME = s.userGroup.name;
        let text = `User ID: ${USER_NAME}, User Group: ${USER_GROUP_NAME}`;
        document.getElementById("user_info").innerHTML = `<a>${text}</a>`;
    });