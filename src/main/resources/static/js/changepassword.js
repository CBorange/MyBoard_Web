function onSubmitChange(){
    const changePasswordForm = document.querySelector('#changePasswordForm');
    const sendData = {
        userID : changePasswordForm.elements['username'].value,
        password : changePasswordForm.elements['cur_password'].value,
        afterPassword : changePasswordForm.elements['change_password'].value,
    };

    let msg = '비밀번호 변경 성공';
    const resultArea = document.querySelector('#resultArea');
    const resultMsg = document.querySelector('#resultMsg');

    var tokenInfo = getCSRFToken();
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append(tokenInfo.header, tokenInfo.token);

    const url = makeURL('/changepassword');
    fetch(url, {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(sendData)
    })
    .then((response) => {
        console.log('onSubmitChange POST API 전송결과 : ', response);
        resultMsg.style.color = "green";
        if(response.status == 400){
            response.text().then(errorMsg => {
                msg = errorMsg;
                resultMsg.style.color = "red";
                console.log('onSubmitChange API 결과 : ' + errorMsg)

                resultArea.style.visibility = "visible";
                resultMsg.innerText = msg;
            })
        }else{
            resultArea.style.visibility = "visible";
            resultMsg.innerText = msg;
        }
    })
    .catch((error) => {
        console.log('onSubmitChange 실패 : ', error);
        resultArea.style.visibility = "visible";
        resultMsg.style.color = "red";
        resultMsg.innerText = error;
    });
}