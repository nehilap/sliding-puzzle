if (!localStorage.grad) {
    localStorage.grad = "0";
}

setWrapperGrad();

setupTiles();

setupHintButton();

setupHints();

function showForm() {
    document.getElementById("newGameForm").classList.remove("hidden");
}

function hideForm() {
    document.getElementById("newGameForm").classList.add("hidden");
}

function toggleTutorial() {
    document.getElementById("gameGuide").classList.toggle("hidden");
}

function toggleHints() {
    if (localStorage.hints == null) {
        localStorage.hints = "1";
    }

    localStorage.hints = localStorage.hints === "1" ? "0" : "1";

    setupHints();
}

function setWrapperGrad() {
    let wrapper = document.getElementById("wrapper");

    wrapper.classList.remove("grad-0", "grad-1", "grad-2", "grad-3");
    wrapper.classList.add(`grad-${localStorage.grad}`)
}

function changeGrad() {
    localStorage.grad = (Number(localStorage.grad) + 1) % 4;

    setWrapperGrad();
}

function setupTiles() {
    let tiles = document.getElementsByClassName("tile");

    if (tiles.length === 0) {
        return;
    }

    if (!imgUrl) {
        if (imgSource !== "numbers") {
            for (let i = 0; i < tiles.length; i++) {
                tiles[i].style.backgroundImage = "url('../images/puzzle/nehila/" + imgSource + "')";
            }
        } else {
            for (let i = 0; i < tiles.length; i++) {
                if (tiles[i].innerText !== "") {
                    tiles[i].style.color = `${colorArray[tiles[i].innerText]}`;
                }
            }
        }
    } else {
        for (let i = 0; i < tiles.length; i++) {
            tiles[i].style.backgroundImage = "url('" + imgUrl + "')";
        }
    }
}

function setupHintButton() {
    if (imgSource === "numbers") {
        document.getElementById("hintButton").classList.add("hidden");
    }
}

function setupHints() {
    let tiles = document.getElementsByClassName("tile");

    for (let i = 0; i < tiles.length; i++) {
        if (localStorage.hints === "1") {
            tiles[i].classList.remove("hint-hidden");
        }else if (localStorage.hints === "0") {
            tiles[i].classList.add("hint-hidden");
        }
    }
}

function fieldAction(row, column) {
    // fetch result is fragment called 'page', so we replace whole view with it - sneaky data update without refresh
    fetch(`/puzzle-nehila/move?row=${row}&column=${column}`)
        .then(response => {
            return response.text();
        }).then(text => {
        document.getElementById("wrapper").innerHTML = text;


        // re-setup everything
        setupTiles();
        setUserRatingsStars();
        setupCommentForm();
        setupHintButton();
        setupHints();

        return Promise.resolve();
    }).then(() => {
        // after everything we check if game is solved so we can show alert
        showSolvedAlert();
    });
}

function showSolvedAlert() {
    // show alert after everything is done and game is solved

    let solvedElm = document.getElementById("solvedMessage"); // value used to check if game is solved
    if (solvedElm && Number(localStorage.alerted) === 0) {
        window.alert("Congratulations, you solved the puzzle");
        localStorage.alerted = 1;
    } else if (!solvedElm) {
        localStorage.alerted = 0;
    }

}