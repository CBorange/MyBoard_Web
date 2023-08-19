// JS 로드된 후 임시 비밀번호 설정 API 날림
let linkParam = '';
window.onload = (event) => {
    var resultMainArea = document.getElementById('resultMain');
    var failedMsgArea = document.getElementById('failedArea');
    var labelUserId = document.getElementById('label_userId');
    var labelPassword = document.getElementById('label_password');

    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');

    var tokenInfo = getCSRFToken();
    if(tokenInfo != null){
        headers.append(tokenInfo.header, tokenInfo.token);
    }

    const url = makeURL('/user/password/change-temporary');
    fetch(url, {
        method: 'PUT',
        headers: headers,
        body: 'linkParam=' + linkParam
    })
    .then((response) => {
        if(response.ok){
            return response.json();
        }
    })
    .then((json) => {
        label_userId.style.color = "green";
        label_userId.innerText = json.userId;

        labelPassword.style.color = "green";
        labelPassword.innerText = json.password;
    })
    .catch((error) => {
        resultMainArea.style.display = "none";
        failedMsgArea.style.display = "block";
    })
}