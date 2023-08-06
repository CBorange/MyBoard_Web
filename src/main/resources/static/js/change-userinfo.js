/*Input Element에 유효성검사 추가*/

// 변경 후 비밀번호
var afterPasswordInput = document.querySelector('#afterPassword');
afterPasswordInput.addEventListener("keyup", (e) => {
    tryMatch_AfterPassword();
})

function tryMatch_AfterPassword(){
    var afterPasswordInput = document.querySelector('#afterPassword');
    // 최소 8자 이상, 영문 및 숫자 포함 필수, 영문 대문자 또는 특수문자 1개이상 포함
    const regexPattern = /^(?=.*[A-Z!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,}$/;
    const match = regexPattern.test(afterPasswordInput.value);

    afterPasswordInput.classList.remove('is-valid');
    afterPasswordInput.classList.remove('is-invalid');
    // Valid
    if(match){
        afterPasswordInput.classList.add('is-valid');
        // Invalid
    } else {
        afterPasswordInput.classList.add('is-invalid');
    }
}

function onSubmitChange(){
    const changeUserInfoForm = document.querySelector('#changeUserInfoForm');
    const sendData = {
        userID : changeUserInfoForm.elements['username'].value,
        email : changeUserInfoForm.elements['email'].value,
        changePassword : changeUserInfoForm.elements['changePassword'].checked,
        password : changeUserInfoForm.elements['cur_password'].value,
        afterPassword : changeUserInfoForm.elements['change_password'].value,
    };

    let msg = '유저정보 변경 성공, 다시 로그인 해주세요';
    const resultArea = document.querySelector('#resultArea');
    const resultMsg = document.querySelector('#resultMsg');
    const goToLoginBtn = document.querySelector('#goToLoginBtn');

    var headers = new Headers();
    headers.append('Content-Type', 'application/json');

    var tokenInfo = getCSRFToken();
    if(tokenInfo != null){
        headers.append(tokenInfo.header, tokenInfo.token);
    }

    const url = makeURL('/changeuserinfo');
    fetch(url, {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(sendData)
    })
    .then((response) => {
        console.log('onSubmitChange POST API 전송결과 : ', response);
        resultMsg.style.color = "green";
        if(response.ok){
            resultArea.style.visibility = "visible";
            goToLoginBtn.style.visibility = "visible";
            resultMsg.innerText = msg;
        }else{
            response.text().then(errorMsg => {
                msg = errorMsg;
                resultMsg.style.color = "red";
                console.log('onSubmitChange API 결과 : ' + errorMsg)

                resultArea.style.visibility = "visible";
                resultMsg.innerText = msg;
            })
        }
    })
    .catch((error) => {
        console.log('onSubmitChange 실패 : ', error);
        resultArea.style.visibility = "visible";
        resultMsg.style.color = "red";
        resultMsg.innerText = error;
    });
}

function onChangedChangePassword(){
    var changePasswordCheckInput = document.querySelector('#changePasswordCheck');
    var afterPasswordInput = document.querySelector('#afterPassword');

    afterPasswordInput.classList.remove('is-valid');
    afterPasswordInput.classList.remove('is-invalid');
    afterPasswordInput.disabled = true;
    if(changePasswordCheckInput.checked){
        afterPasswordInput.disabled = false;
        tryMatch_AfterPassword();
    }
}