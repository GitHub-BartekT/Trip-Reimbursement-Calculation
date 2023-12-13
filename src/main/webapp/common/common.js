function newCellInRow(row, int, text){
    let newCell = row.insertCell(int);
    let newText = document.createTextNode(text);
    newCell.appendChild(newText);
}

function processOkResponse(response = {}) {
    if (response.ok) {
        return response.json();
    }
    throw new Error(`Status not 200 (${response.status})`);
}

