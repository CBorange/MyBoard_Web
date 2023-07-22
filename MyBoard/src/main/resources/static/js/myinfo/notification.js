function readNotification(notificationId){
    const url = makeURL('/user/notification/' + notificationId + '/read');

    var tokenInfo = getCSRFToken();
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append(tokenInfo.header, tokenInfo.token);

    fetch(url, {
            method: 'POST',
            headers: headers,
        })
        .then((response) => {
            console.log('readNotification 성공 : ', response);
            if(response.redirected)
                window.location.href = response.url;
        })
        .catch((error) => {
            console.log('readNotification 실패 : ', error);
        })
}