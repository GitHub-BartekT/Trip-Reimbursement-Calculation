loadUserHeader();

function loadUserHeader() {
    fetch('../common/user_header.html')
        .then(response => response.text())
        .then(data => {
            document.getElementById('header').innerHTML = data;
        })
        .catch(error => console.error('Error:', error));
}