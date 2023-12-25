function loadHeader() {
    return new Promise((resolve, reject) => {
        fetch('../common/administrator_header.html')
            .then(response => response.text())
            .then(data => {
                document.getElementById('header').innerHTML = data;
                resolve ();
            })
            .catch(error => {
                console.error('Error:', error);
            });
    });
}



function startAdministrator(){
    loadHeader().then(r => {
            return readDataFromUrl();
        })
        .then(loggedUserId => {
            return readLoggedUserById(loggedUserId);
        })
        .catch(error => {
            console.error(error);
        });
}

