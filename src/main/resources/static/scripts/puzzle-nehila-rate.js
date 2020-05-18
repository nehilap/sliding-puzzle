let userRatingsStars;

setUserRatingsStars();

function setRate(rate) {
    let rating = {
        player: loggedUser,
        game: 'puzzle-nehila',
        ratedon: new Date(),
        rating: rate
    };
    const options = {
        method: "POST",
        body: JSON.stringify(rating),
        headers: {
            "content-type": "application/json",
            "accept": "text/plain"
        }
    };

    fetch("/puzzle-nehila/rating", options)
        .then(response => {
            return response.text();
        }).then(text => {
        document.getElementById("ratings").innerHTML = text;

        setUserRatingsStars();
        return Promise.resolve();
    });
}

function handleStarHover(index) {
    for (let i = 0; i < userRatingsStars.length; i++) {
        if (Number(userRatingsStars[i].getAttribute("starindex")) < index) {
            userRatingsStars[i].classList.add("shiny");
        }
    }
}

function handleStarMouseOut() {
    for (let i = 0; i < userRatingsStars.length; i++) {
        userRatingsStars[i].classList.remove("shiny");
    }
}

function setUserRatingsStars() {
    userRatingsStars = document.getElementsByClassName("player-stars");

    if (userRatingsStars.length > 0) {
        userRatingsStars = userRatingsStars[0].children;
    }
}