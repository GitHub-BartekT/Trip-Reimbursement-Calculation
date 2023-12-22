startUser();

function startUser(){
    readDataFromUrl()
        .then(loggedUserId => {
            return Promise.all([
                readAllReimbursement(loggedUserId),
                readLoggedUserByIdReturnUserGroupId(loggedUserId)
                ]);
        })
        .catch(error => {
            console.error(error);
        });
}