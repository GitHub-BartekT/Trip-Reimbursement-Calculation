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