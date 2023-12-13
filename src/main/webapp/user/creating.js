function addReimbursement(){
    let reimb_name = document.getElementById(`reimbursement_name`).value;
    let reimb_startDate = document.getElementById(`reimbursement_start_day`).value;
    let reimb_endDate= document.getElementById(`reimbursement_end_day`).value;
    let reimb_distance= document.getElementById(`reimbursement_distance`).value;

    doPostReimbursement(reimb_name, reimb_startDate, reimb_endDate, reimb_distance);
}

function doPostReimbursement(reimb_name, reimb_startDate, reimb_endDate, reimb_distance){
    let bodyAddReimbursement = {
        name: reimb_name,
        startDate:  reimb_startDate,
        endDate: reimb_endDate,
        distance: reimb_distance,
        userId: USER_ID
    };

    fetch(`${REIMBURSEMENTS_API_URL}`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(bodyAddReimbursement)
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(error => {
                    document.getElementById('information').innerHTML = `<h2>Adding a new reimbursement failed!</h2>`;
                });
            }
            const locationHeader = response.headers.get('Location');
            if (locationHeader) {
                const urlParts = locationHeader.split('/');
                const newObjectId = urlParts[urlParts.length - 1];
                const topTextContainer = document.getElementById('top-text-container');
                topTextContainer.innerHTML = `<h2>Your reimbursement id:${newObjectId}</h2>`;
            }
            document.getElementById('information').innerHTML = `<h2>You added a new reimbursements!</h2>`;
        })
        .catch(console.warn);
}
