// 게시글 링크 표기
window.onload = function(){
    var linkElement = document.getElementById("postLink");
    var curURL = window.location.toString();
    linkElement.href = curURL;
    linkElement.innerText = curURL;
}

// 대댓글 작성 form toggle
function onClickShowReplyForm(id){
    var subReplyForm = document.getElementById("subReplyForm_" + id);
    if(subReplyForm.style.display == "none")
        subReplyForm.style.display = "block";
    else
        subReplyForm.style.display = "none";
}

// 댓글/대댓글 submit request 전송
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

// 게시글 삭제 request 전송
function onClickDeletePost(boardID, postID) {
    const url = makeURL('/post/' + postID);
    fetch(url, {
        method: 'DELETE'
    })
    .then((response) => {
        console.log('onClickDeletePost 성공 : ', response);
        // 게시판으로 redirect
        window.location.href = "/board/" + boardID;
    })
    .catch((error) => {
        console.log('onClickDeletePost 실패 : ', error);
    })
}