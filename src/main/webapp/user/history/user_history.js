startUserHistory();

function startUserHistory(){
    loadUserHeader().then(r => {
        return readDataFromUrl();
    })
        .then(loggedUserId => {
            return Promise.all([
                readOptionalReimbursement(true, loggedUserId),
                readLoggedUserById(loggedUserId)
            ]);
        })
        .catch(error => {
            console.error(error);
        });
}
