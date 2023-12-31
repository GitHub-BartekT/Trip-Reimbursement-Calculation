startUser();

function startUser(){
    loadUserHeader().then(r => {
        return readDataFromUrl();
    })
        .then(loggedUserId => {
            return Promise.all([
                readOptionalReimbursement(false, loggedUserId),
                readLoggedUserById(loggedUserId)
            ]);
        })
        .catch(error => {
            console.error(error);
        });
}


