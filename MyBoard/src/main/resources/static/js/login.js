function onSubmitLogin(){
    const registerForm = document.querySelector('#loginForm');
    const sendData = {
        userID : registerForm.elements['username'].value,
        password : registerForm.elements['password'].value,
    };

    let msg = '로그인 성공';
    const resultArea = document.querySelector('#resultArea');
    const resultMsg = document.querySelector('#resultMsg');

    const url = makeURL('/login');
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(sendData)
    })
    .then((response) => {
        console.log('onSubmitLogin POST API 전송결과 : ', response);
        resultMsg.style.color = "green";
        if(response.status == 400){
            response.text().then(errorMsg => {
                msg = errorMsg;
                resultMsg.style.color = "red";
                console.log('onSubmitLogin API 결과 : ' + errorMsg)

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
        console.log('onSubmitLogin 실패 : ', error);
        resultArea.style.visibility = "visible";
        resultMsg.style.color = "red";
        resultMsg.innerText = error;
    });
}