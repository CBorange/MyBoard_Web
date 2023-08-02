/*여기에 유저정보 보기, 게시글검색 API 호출, fragement Parameter로 받은 userName 활용*/
function onClickUserInfo(userId){
    var headers = new Headers();

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