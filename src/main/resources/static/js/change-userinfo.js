/*Input Element에 유효성검사 추가*/
var afterPasswordInput = document.querySelector('#afterPassword');
afterPasswordInput.addEventListener("keyup", (e) => {
    validation_AfterPassword();
})
var emailInput = document.querySelector('#userEmailInput');
emailInput.addEventListener("keyup", (e) => {
    validation_Email();
})

// 이메일
function validation_Email(){
    var emailInput = document.querySelector('#userEmailInput');

    const match = tryMatch_Email();

    emailInput.classList.remove('is-valid');
    emailInput.classList.remove('is-invalid');
    // Valid
    if(match){
        emailInput.classList.add('is-valid');
        // Invalid
    } else {
        emailInput.classList.add('is-invalid');
    }
}

function tryMatch_Email(){
    var emailInput = document.querySelector('#userEmailInput');
    // 이메일 유효성 검사 Regex
    const regexPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    const match = regexPattern.test(emailInput.value);
    return match;
}

// 변경 후 비밀번호
function validation_AfterPassword(){
    // 비밀번호 수정 여부가 check일 경우에면 유효성 검사
    var changePasswordCheckInput = document.querySelector('#changePasswordCheck');
    if(!changePasswordCheckInput.checked)
        return;

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
    const changeUserInfoForm = document.querySelector('#changeUserInfoForm');

    // 유효성 검사
    validation_Email();
    validation_AfterPassword();
    var inputList = document.querySelectorAll('#changeUserInfoForm input');
    for(inputTag of inputList){
        if(inputTag.classList.contains('is-invalid')){
            alert('적용할 수 없습니다. 잘못된 부분을 수정한 후 다시 시도해주세요.');
            return;
        }
    }

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
        validation_AfterPassword();
    }
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