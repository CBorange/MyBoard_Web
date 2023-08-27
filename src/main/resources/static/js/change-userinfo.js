// input element id 선언
const nicknameInputId = '#nickname';
const emailInputId = '#userEmailInput';

/*Input Element에 유효성검사 추가*/
var nicknameInput = document.querySelector(nicknameInputId);
nicknameInput.addEventListener("keyup", (e) => {
    validationInput(nicknameInputId, nicknameRegexExp);
});

var emailInput = document.querySelector('#userEmailInput');
emailInput.addEventListener("keyup", (e) => {
    validationInput(emailInputId, emailRegexExp);
});

function onSubmitChange(){
    const changeUserInfoForm = document.querySelector('#changeUserInfoForm');

    // 유효성 검사
    validationInput(nicknameInputId, nicknameRegexExp);
    validationInput(emailInputId, emailRegexExp);
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
    const submitBtn = document.querySelector('#submitBtn');

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
            submitBtn.style.visibility = 'hidden';
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