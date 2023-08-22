function readNotification(notificationId){
    const url = makeURL('/user/notification/' + notificationId + '/read');

    var headers = new Headers();
    headers.append('Content-Type', 'application/json');

    var tokenInfo = getCSRFToken();
    if(tokenInfo != null){
        headers.append(tokenInfo.header, tokenInfo.token);
    }

    fetch(url, {
            method: 'PATCH',
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

function readAllNotification(userId){
    const url = makeURL('/user/notification/read');

    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');

    var tokenInfo = getCSRFToken();
    if(tokenInfo != null){
        headers.append(tokenInfo.header, tokenInfo.token);
    }

    fetch(url, {
                method: 'PUT',
                headers: headers,
                body: 'userId=' + userId
            })
            .then((response) => {
                if(response.ok){
                    console.log('readAllNotification 성공 : ', response);
                    alert("모든 알림이 읽음처리 되었습니다.");
                    window.location.reload();
                }
                else {
                    alert("알림 모두읽기 실패");
                }
            })
            .catch((error) => {
                console.log('readAllNotification 실패 : ', error);
            })
}