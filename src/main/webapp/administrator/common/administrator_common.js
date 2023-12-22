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

function deleteRows(rowIndex, elementId) {
    const table = document.getElementById(elementId);
    console.info(table.rows.length);
    if ((rowIndex >= 0) && (rowIndex < table.rows.length)) {
        for (let i = table.rows.length - 1; i >= rowIndex; i--) {
            table.deleteRow(i);
        }
    }
}

function changePlaceholderAndValue(id, text){
    document.getElementById(id).setAttribute("placeholder", text);
    document.getElementById(id).setAttribute("value", text);
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

