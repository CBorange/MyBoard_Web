window.onload = function(){
    var linkElement = document.getElementById("postLink");
    var curURL = window.location.toString();
    linkElement.href = curURL;
    linkElement.innerText = curURL;
}

function onClickShowReplyForm(id){
    var subReplyForm = document.getElementById("subReplyForm_" + id);
    if(subReplyForm.style.display == "none")
        subReplyForm.style.display = "block";
    else
        subReplyForm.style.display = "none";
}

function onSubmitComment(rootCommentID, isSubComment) {
    var formID = "#subReplyForm";
    if(isSubComment)
        formID = "#subReplyForm_" + rootCommentID;
    const writeCommentForm = document.querySelector(formID);

    const sendData = {
        postID: writeCommentForm.elements['postID'].value,
        parentCommentID: rootCommentID,
        writerID: writeCommentForm.elements['writerID'].value,
        content: writeCommentForm.elements['content'].value,
    };
    const url = makeURL('/comment');
    fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(sendData)
    })
    .then((response) => {
        console.log('onSubmitComment 성공 : ', response);
        if(response.redirected)
            window.location.href = response.url;
    })
    .catch((error) => {
        console.log('onSubmitComment 실패 : ', error);
    })
}