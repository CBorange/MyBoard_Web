function onSubmitRegister(){
    const registerForm = document.querySelector('#registerForm');
    const sendData = {
        email : registerForm.elements['email'].value,
        nickname : registerForm.elements['nickname'].value,
        userID : registerForm.elements['username'].value,
        password : registerForm.elements['password'].value,
    };

    let msg = '회원가입 성공, 아래 버튼을 클릭하여 로그인 해주세요.';
    const resultArea = document.querySelector('#resultArea');
    const resultMsg = document.querySelector('#resultMsg');
    const loginButton = document.querySelector('#loginButton');

    var tokenInfo = getCSRFToken();
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append(tokenInfo.header, tokenInfo.token);

    const url = makeURL('/register');
    fetch(url, {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(sendData)
    })
    .then((response) => {
        console.log('onSubmitRegister POST API 전송결과 : ', response);
        resultMsg.style.color = "green";
        if(response.status == 400){
            response.text().then(errorMsg => {
                msg = errorMsg;
                resultMsg.style.color = "red";
                console.log('onSubmitRegister API 결과 : ' + errorMsg)

                resultArea.style.visibility = "visible";
                resultMsg.innerText = msg;
            })
        }else{
            resultArea.style.visibility = "visible";
            resultMsg.innerText = msg;
            loginButton.style.visibility = "visible";
        }
    })
    .catch((error) => {
        console.log('onSubmitRegister 실패 : ', error);
        resultArea.style.visibility = "visible";
        resultMsg.style.color = "red";
        resultMsg.innerText = error;
    });
}