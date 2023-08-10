/*Input Element에 유효성검사 추가*/
var nicknameInput = document.querySelector('#nickname');
nicknameInput.addEventListener("keyup", (e) => {
    validation_Nickname();
});

var emailInput = document.querySelector('#userEmailInput');
emailInput.addEventListener("keyup", (e) => {
    validation_Email();
});

// 닉네임
function validation_Nickname(){
    var nicknameInput = document.querySelector('#nickname');

    const match = tryMatch_Nickname();

    nicknameInput.classList.remove('is-valid');
    nicknameInput.classList.remove('is-invalid');
    // Valid
    if(match){
        nicknameInput.classList.add('is-valid');
        // Invalid
    } else {
        nicknameInput.classList.add('is-invalid');
    }
}
function tryMatch_Nickname(){
    var nicknameInput = document.querySelector('#nickname');

    // 이메일 유효성 검사 Regex
    const regexPattern = /^(?=.*[a-zA-Z0-9가-힣])[a-zA-Z0-9가-힣]{2,12}$/;
    const match = regexPattern.test(nicknameInput.value);
    return match;
}

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

function onSubmitChange(){
    const changeUserInfoForm = document.querySelector('#changeUserInfoForm');

    // 유효성 검사
    validation_Email();
    validation_Nickname();
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
        nickname : changeUserInfoForm.elements['nickname'].value,
        curPassword : changeUserInfoForm.elements['cur_password'].value,
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

    const url = makeURL('/user');
    fetch(url, {
        method: 'PATCH',
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