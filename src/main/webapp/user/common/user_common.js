function loadUserHeader() {
    return new Promise((resolve, reject) => {
    fetch('../common/user_header.html')
        .then(response => response.text())
        .then(data => {
            document.getElementById('header').innerHTML = data;
            resolve ();
        })
        .catch(error => console.error('Error:', error));
    });
}

function readUserGroupByIdToTable(loggedUserGroupId) {
    return new Promise(resolve => {
        fetch(`${USER_GROUPS_API_URL}/${loggedUserGroupId}`)
            .then(response => response.json())
            .then((s) => {
                let table = document.getElementById('user_group_params_table');
                let row = table.insertRow(-1);
                newCellInRow(row, 0, s.name);
                newCellInRow(row, 1, s.dailyAllowance);
                newCellInRow(row, 2, s.costPerKm);
                newCellInRow(row, 3, s.maxMileage);
                newCellInRow(row, 4, s.maxRefund);

            });resolve();
    });
}
