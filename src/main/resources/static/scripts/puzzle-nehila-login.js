function handleUser(action) {
    const pass = document.getElementById("userPass").value;

    let user = {
        name: document.getElementById("userName").value,
        password: CryptoJS.RIPEMD160(CryptoJS.SHA3(CryptoJS.MD5(pass).toString() + pass).toString() + pass).toString()
    };

    const options = {
        method: "POST",
        body: JSON.stringify(user),
        headers: {
            "content-type": "application/json",
            "accept": "text/plain"
        }
    };

    fetch("/player/" + action, options)
        .then(response => {
            return response.text();
        }).then(text => {
        document.getElementById("message").innerHTML = text;

        document.getElementById("userPass").value = "";

        // we either failed miserably or login is successful
        if (!text) {
            window.location = "/";
        }
        return Promise.resolve();
    });
}