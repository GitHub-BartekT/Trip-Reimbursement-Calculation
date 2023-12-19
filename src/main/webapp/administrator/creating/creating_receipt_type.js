readDataFromUrl();
readUserById();
setMode();

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
                    document.getElementById('information').innerHTML = `<h2>Adding a new Receipt Type failed!</h2>`;
                });
            }
            const locationHeader = response.headers.get('Location');
            if (locationHeader) {
                const urlParts = locationHeader.split('/');
                RECEIPT_TYPE_ID = urlParts[urlParts.length - 1];
                pageChangingModeReceiptType();
            }
            document.getElementById('information').innerHTML = `<h2>You added a new Receipt Type!</h2>`;
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
                document.getElementById('information').innerHTML = `<h2>You changed the Receipt Type!</h2>`;
            } else {
                document.getElementById('information').innerHTML = `<h2>Changing was failed!</h2>`;
            }
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
                document.getElementById('information').innerHTML = `<h2>You deleted the Receipt Type!</h2>`;
            } else {
                document.getElementById('information').innerHTML = `<h2>Deleting was failed!</h2>`;
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
    changeBtnToDisable("add_cost_btn");
    changeBtnToDisableDelete("delete_btn");
}

function pageChangingModeReceiptType(){
    CREATE_MODE = false;
    const topTextContainer = document.getElementById('top-text-container');
    topTextContainer.innerHTML = `<h2>Modified receipt type id:${RECEIPT_TYPE_ID}</h2>`;
    document.getElementById("accept_btn").innerText = "Save changes";
    changeBtnToPrimary("add_cost_btn");
    changeBtnToDelete("delete_btn");
}
