function onClickLogout(){
    var headers = new Headers();

    var tokenInfo = getCSRFToken();
    if(tokenInfo != null){
        headers.append(tokenInfo.header, tokenInfo.token);
    }

    const url = makeURL('/logout');
        fetch(url,{
            method: 'POST',
            headers: headers,
        })
        .then((response) => {
            if(response.ok){
                if(response.redirected)
                    window.location.href = response.url;
            }
        })
        .catch((error) => {
            console.log('onClickLogout 실패 : ', error);

            response.text()
                .then((text) => {
                    alert(text);
                })

            // refresh
            window.location.reload();
        })
}