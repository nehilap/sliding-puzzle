let commentForm;

setupCommentForm();

function setupCommentForm() {
    commentForm = document.getElementById("commentForm");

    if (commentForm) {
        commentForm.addEventListener("submit", commentFormHandler);
    }

}

function commentFormHandler(event) {
    event.preventDefault();

    let comment = {
        player: loggedUser,
        game: 'puzzle-nehila',
        commentedOn: new Date(),
        comment: commentForm.elements.namedItem("comm-text").value
    };
    const options = {
        method: "POST",
        body: JSON.stringify(comment),
        headers: {
            "content-type": "application/json",
            "accept": "text/plain"
        }
    };

    // why use JQuery when we can waste 5 hours doing it in vanilla right? Only to find out it's 5 rows of code....
    fetch("/puzzle-nehila/comment", options)
        .then(response => {
            return response.text();
        }).then(text => {
        document.getElementById("comments").innerHTML = text;
        commentForm.reset();
        return Promise.resolve();
    });
}