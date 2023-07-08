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
        postId: writeCommentForm.elements['postID'].value,
        parentCommentId: rootCommentID,
        writerId: writeCommentForm.elements['writerID'].value,
        writerNickname: writeCommentForm.elements['writerNickname'].value,
        content: writeCommentForm.elements['content'].value,
    };
    const url = makeURL('/comment');

    var tokenInfo = getCSRFToken();
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append(tokenInfo.header, tokenInfo.token);

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

// 게시글 삭제 request 전송
function onClickDeletePost(boardID, postID) {
    var tokenInfo = getCSRFToken();
    var headers = new Headers();
    headers.append(tokenInfo.header, tokenInfo.token);

    const url = makeURL('/post/' + postID);
    fetch(url, {
        method: 'DELETE',
        headers: headers,
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

// 게시글 추천 request 전송
function onClickLike(postId, userId){
    if(userId == null) {
        alert("추천할 수 없습니다. 먼저 로그인 해주세요.");
        return;
    }

    var tokenInfo = getCSRFToken();
    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');
    headers.append(tokenInfo.header, tokenInfo.token);

    const url = makeURL('/post/' + postId + '/like');
    fetch(url,{
        method: 'POST',
        headers: headers,
        body: 'userId=' + userId
    })
    .then((response) => {
        if(response.status == 400){
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
    })
}

// 게시글 비추천 request 전송
function onClickDislike(postId, userId){
    if(userId == null) {
        alert("비추천할 수 없습니다. 먼저 로그인 해주세요.");
        return;
    }

    var tokenInfo = getCSRFToken();
    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');
    headers.append(tokenInfo.header, tokenInfo.token);

    const url = makeURL('/post/' + postId + '/dislike');
    fetch(url,{
        method: 'POST',
        headers: headers,
        body: 'userId=' + userId
    })
    .then((response) => {
        if(response.status == 400){
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