// 게시글 링크 표기
window.onload = function(){
    var linkElement = document.getElementById("postLink");
    var curURL = window.location.toString();
    linkElement.href = curURL;
    linkElement.innerText = curURL;

    // 댓글 작성 후 새로고침된 경우
    var commentSubmitted = sessionStorage.getItem("commentSubmitted");
    if(commentSubmitted){
        sessionStorage.removeItem("commentSubmitted");

        // 댓글 영역 상단으로 스크롤
        //var location = document.querySelector("#commentTitleDiv").offsetTop;
        //window.scrollTo({top:location, behavior: 'auto'});
    }
}

// 댓글 수정 form toggle
function onClickShowModifyCommentForm(id){
    var commentBody = document.getElementById("comment_" + id);
    var modifyCommentForm = document.getElementById("modifyCommentForm_" + id);

    if(modifyCommentForm.style.display == "none"){
        commentBody.style.display = "none";
        modifyCommentForm.style.display = "block";
    }
    else{
        commentBody.style.display = "block";
        modifyCommentForm.style.display = "none";
    }
}

// 댓글 수정 request 전송
function onModifyComment(commentId){
    var textareaElement = document.getElementById("modifyCommentForm_TextArea_" + commentId);
    var newContent = textareaElement.value;

    const url = makeURL("/comment/" + commentId + "/content");

    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');

    var tokenInfo = getCSRFToken();
    if(tokenInfo != null){
        headers.append(tokenInfo.header, tokenInfo.token);
    }

    fetch(url, {
        method: 'POST',
        headers: headers,
        body: 'newContent=' + newContent
    })
    .then((response) => {
        console.log('onModifyComment 성공 : ', response);
        window.location.reload();
    })
    .catch((error) => {
        console.log('onModifyComment 실패 : ', error);
    })
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
        postWriterId: writeCommentForm.elements['postWriterID'].value,
        postId: writeCommentForm.elements['postID'].value,
        parentCommentId: rootCommentID,
        writerId: writeCommentForm.elements['writerID'].value,
        content: writeCommentForm.elements['content'].value,
        isSubComment: isSubComment,
    };
    const url = makeURL('/comment');

    var headers = new Headers();
    headers.append('Content-Type', 'application/json');

    var tokenInfo = getCSRFToken();
    if(tokenInfo != null){
        headers.append(tokenInfo.header, tokenInfo.token);
    }

    fetch(url, {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(sendData)
    })
    .then((response) => {
        console.log('onSubmitComment 성공 : ', response);
        sessionStorage.setItem("commentSubmitted", "true");
        window.location.reload();
    })
    .catch((error) => {
        console.log('onSubmitComment 실패 : ', error);
    })
}

// 댓글 삭제 Request 전송
function onClickDeleteComment(commentId){
    var isOk = confirm("정말로 댓글을 삭제하시겠습니까?");
    if(!isOk)
        return;

    var headers = new Headers();
    var tokenInfo = getCSRFToken();
    if(tokenInfo != null){
        headers.append(tokenInfo.header, tokenInfo.token);
    }

    const url = makeURL('/comment/' + commentId);
    fetch(url, {
        method: 'DELETE',
        headers: headers,
    })
    .then((response) => {
        if(response.ok){
            console.log('onClickDeleteComment 성공 : ', response);

            // 게시글 reload
            window.location.reload();
        }
    })
    .catch((error) => {
        console.log('onClickDeleteComment 실패 : ', error);
    })
}

// 게시글 삭제 request 전송
function onClickDeletePost(boardID, postID) {
    var isOk = confirm("정말로 게시글을 삭제하시겠습니까?");
    if(!isOk) 
        return;

    var headers = new Headers();
    var tokenInfo = getCSRFToken();
    if(tokenInfo != null){
        headers.append(tokenInfo.header, tokenInfo.token);
    }

    const url = makeURL('/post/' + postID);
    fetch(url, {
        method: 'DELETE',
        headers: headers,
    })
    .then((response) => {
        if(response.ok){
            console.log('onClickDeletePost 성공 : ', response);
            // 게시판으로 redirect
            window.location.href = "/board/" + boardID;
        }
    })
    .catch((error) => {
        console.log('onClickDeletePost 실패 : ', error);
    })
}

// 게시글 추천 request 전송
function onClickLike(postId, userId){
    if(userId == null) {
        alert("추천할 수 없습니다. 먼저 로그인 해주세요.");
        return;
    }

    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');

    var tokenInfo = getCSRFToken();
    if(tokenInfo != null){
        headers.append(tokenInfo.header, tokenInfo.token);
    }

    const url = makeURL('/post/' + postId + '/like');
    fetch(url,{
        method: 'POST',
        headers: headers,
        body: 'userId=' + userId
    })
    .then((response) => {
        if(!response.ok){
            response.text()
                .then((text) => {
                    alert(text);
                })
        }

        // refresh
        window.location.reload();
    })
    .catch((error) => {
        console.log('onClickLike 실패 : ', error);

        response.text()
            .then((text) => {
                alert(text);
            })

        // refresh
        window.location.reload();
    })


}

// 게시글 비추천 request 전송
function onClickDislike(postId, userId){
    if(userId == null) {
        alert("비추천할 수 없습니다. 먼저 로그인 해주세요.");
        return;
    }

    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');

    var tokenInfo = getCSRFToken();
    if(tokenInfo != null){
        headers.append(tokenInfo.header, tokenInfo.token);
    }

    const url = makeURL('/post/' + postId + '/dislike');
    fetch(url,{
        method: 'POST',
        headers: headers,
        body: 'userId=' + userId
    })
    .then((response) => {
        if(!response.ok){
            response.text()
                .then((text) => {
                    alert(text);
                })
        }
        // refresh
        window.location.reload();
    })
    .catch((error) => {
        console.log('onClickDislike 실패 : ', error);
    })
}

// 댓글 추천 request 전송
function onClickCommentLike(commentId, userId){
    if(userId == null) {
        alert("댓글을 추천할 수 없습니다. 먼저 로그인 해주세요.");
        return;
    }

    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');

    var tokenInfo = getCSRFToken();
    if(tokenInfo != null){
        headers.append(tokenInfo.header, tokenInfo.token);
    }

    const url = makeURL('/comment/' + commentId + '/like');
    fetch(url,{
        method: 'POST',
        headers: headers,
        body: 'userId=' + userId
    })
    .then((response) => {
        if(!response.ok){
            response.text()
                .then((text) => {
                    alert(text);
                })
        }

        // refresh
        window.location.reload();
    })
    .catch((error) => {
        console.log('onClickCommentLike 실패 : ', error);

        response.text()
            .then((text) => {
                alert(text);
            })

        // refresh
        window.location.reload();
    })


}

// 댓글 비추천 request 전송
function onClickCommentDislike(commentId, userId){
    if(userId == null) {
        alert("댓글을 비추천할 수 없습니다. 먼저 로그인 해주세요.");
        return;
    }

    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');

    var tokenInfo = getCSRFToken();
    if(tokenInfo != null){
        headers.append(tokenInfo.header, tokenInfo.token);
    }

    const url = makeURL('/comment/' + commentId + '/dislike');
    fetch(url,{
        method: 'POST',
        headers: headers,
        body: 'userId=' + userId
    })
    .then((response) => {
        if(!response.ok){
            response.text()
                .then((text) => {
                    alert(text);
                })
        }
        // refresh
        window.location.reload();
    })
    .catch((error) => {
        console.log('onClickCommentDislike 실패 : ', error);
    })
}

// 게시글 스크랩
function onClickSaveScrap(postId, userId){
var headers = new Headers();
    // 스크랩 메모 textarea값 획득
    var remark = document.getElementById('scrap_remark_textarea').value;

    headers.append('Content-Type', 'application/x-www-form-urlencoded');

    var tokenInfo = getCSRFToken();
    if(tokenInfo != null){
        headers.append(tokenInfo.header, tokenInfo.token);
    }

    const url = makeURL('/post/' + postId + '/scrap');
    fetch(url,{
        method: 'POST',
        headers: headers,
        body: 'userId=' + userId + '&remark=' + remark
    })
    .then((response) => {
        if(!response.ok){
            response.text()
                .then((text) => {
                    alert(text);
                })
        } else{
            alert("스크랩 되었습니다.");
        }
        // refresh
        window.location.reload();
    })
    .catch((error) => {
        console.log('onClickSaveScrap 실패 : ', error);
        alert(error);
    })
}

