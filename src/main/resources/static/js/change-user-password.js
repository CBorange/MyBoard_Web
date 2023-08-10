/*Input Element에 유효성검사 추가*/
var afterPasswordInput = document.querySelector('#afterPassword');
afterPasswordInput.addEventListener("keyup", (e) => {
    validation_AfterPassword();
})

// 변경 후 비밀번호
function validation_AfterPassword(){
    var afterPasswordInput = document.querySelector('#afterPassword');

    const match = tryMatch_AfterPassword();

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

function tryMatch_AfterPassword(){
    var afterPasswordInput = document.querySelector('#afterPassword');
    // 비밀번호 유효성 검사 Regex
    // 최소 8자 이상, 영문 및 숫자 포함 필수, 영문 대문자 또는 특수문자 1개이상 포함
    const regexPattern = /^(?=.*[A-Z!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,}$/;
    const match = regexPattern.test(afterPasswordInput.value);
    return match;
}

function onSubmitChange(){
    const changeUserPasswordForm = document.querySelector('#changeUserPasswordForm');

    // 유효성 검사
    validation_AfterPassword();
    var inputList = document.querySelectorAll('#changeUserPasswordForm input');
    for(inputTag of inputList){
        if(inputTag.classList.contains('is-invalid')){
            alert('적용할 수 없습니다. 잘못된 부분을 수정한 후 다시 시도해주세요.');
            return;
        }
    }

    const sendData = {
        userId : changeUserPasswordForm.elements['username'].value,
        curPassword : changeUserPasswordForm.elements['cur_password'].value,
        afterPassword : changeUserPasswordForm.elements['change_password'].value,
    };

    let msg = '비밀번호 변경 성공, 다시 로그인 해주세요';
    const resultArea = document.querySelector('#resultArea');
    const resultMsg = document.querySelector('#resultMsg');
    const goToLoginBtn = document.querySelector('#goToLoginBtn');

    var headers = new Headers();
    headers.append('Content-Type', 'application/json');

    var tokenInfo = getCSRFToken();
    if(tokenInfo != null){
        headers.append(tokenInfo.header, tokenInfo.token);
    }

    const url = makeURL('/user/password');
    fetch(url, {
        method: 'PUT',
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

function onClickPasswordEye(inputSelector, eyeImgSelector){
    var passwordInput = document.querySelector(inputSelector);
    var eyeImgTag = document.querySelector(eyeImgSelector);

    if(passwordInput.getAttribute('type') == 'text'){
        passwordInput.setAttribute('type', 'password');
        eyeImgTag.setAttribute('src', '/img/eye-x.png');
    }
    else{
        passwordInput.setAttribute('type', 'text');
        eyeImgTag.setAttribute('src', '/img/eye.png');
    }
}