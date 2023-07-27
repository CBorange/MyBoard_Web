function readNotification(notificationId){
    const url = makeURL('/user/notification/' + notificationId + '/read');

    var headers = new Headers();
    headers.append('Content-Type', 'application/json');

    var tokenInfo = getCSRFToken();
    if(tokenInfo != null){
        headers.append(tokenInfo.header, tokenInfo.token);
    }

    fetch(url, {
            method: 'POST',
            headers: headers,
        })
        .then((response) => {
            if(response.ok){
                console.log('readNotification 성공 : ', response);
                if(response.redirected)
                    window.location.href = response.url;
            }
            else {
                alert("게시글 또는 댓글이 삭제되었습니다.");
            }
        })
        .catch((error) => {
            console.log('readNotification 실패 : ', error);
        })
}