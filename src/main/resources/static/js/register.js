// input element id 선언
const userIdInputId = '#userIdInput';
const passwordInputId = '#passwordInput';
const nicknameInputId = '#nicknameInput';
const emailInputId = '#emailInput';

/*Input Element에 유효성검사 추가*/
var userIdInput = document.querySelector(userIdInputId);
userIdInput.addEventListener("keyup", (e) => {
    validationInput(userIdInputId, userIdRegexExp);
});

var passwordInput = document.querySelector(passwordInputId);
passwordInput.addEventListener("keyup", (e) => {
    validationInput(passwordInputId, passwordRegexExp);
});

var nicknameInput = document.querySelector(nicknameInputId);
nicknameInput.addEventListener("keyup", (e) => {
    validationInput(nicknameInputId, nicknameRegexExp);
});

var emailInput = document.querySelector(emailInputId);
emailInput.addEventListener("keyup", (e) => {
    validationInput(emailInputId, emailRegexExp);
});

function onSubmitRegister(){
    // 유효성 검사
    validationInput(userIdInputId, userIdRegexExp);
    validationInput(passwordInputId, passwordRegexExp);
    validationInput(nicknameInputId, nicknameRegexExp);
    validationInput(emailInputId, emailRegexExp);
    var inputList = document.querySelectorAll('#registerForm input');
    for(inputTag of inputList){
        if(inputTag.classList.contains('is-invalid')){
            alert('적용할 수 없습니다. 잘못된 부분을 수정한 후 다시 시도해주세요.');
            return;
        }
    }

    const registerForm = document.querySelector('#registerForm');
    const sendData = {
        email : registerForm.elements['email'].value,
        nickname : registerForm.elements['nickname'].value,
        userID : registerForm.elements['userId'].value,
        password : registerForm.elements['password'].value,
    };

    let msg = '회원가입 성공, 아래 버튼을 클릭하여 로그인 해주세요.';
    const resultArea = document.querySelector('#resultArea');
    const resultMsg = document.querySelector('#resultMsg');
    const loginButton = document.querySelector('#goToLoginBtn');

    var headers = new Headers();
    headers.append('Content-Type', 'application/json');

    var tokenInfo = getCSRFToken();
    if(tokenInfo != null){
        headers.append(tokenInfo.header, tokenInfo.token);
    }

    const url = makeURL('/user');
    fetch(url, {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(sendData)
    })
    .then((response) => {
        console.log('onSubmitRegister POST API 전송결과 : ', response);
        resultMsg.style.color = "green";
        if(response.ok){
            resultArea.style.visibility = "visible";
                resultMsg.innerText = msg;
                loginButton.style.visibility = "visible";
        }else{
            response.text().then(errorMsg => {
                msg = errorMsg;
                resultMsg.style.color = "red";
                console.log('onSubmitRegister API 결과 : ' + errorMsg)

                resultArea.style.visibility = "visible";
                resultMsg.innerText = msg;
            });
        }
    })
    .catch((error) => {
        console.log('onSubmitRegister 실패 : ', error);
        resultArea.style.visibility = "visible";
        resultMsg.style.color = "red";
        resultMsg.innerText = error;
    });
}