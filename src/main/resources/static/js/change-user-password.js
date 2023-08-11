// input element id 선언
const afterPasswordInputId = '#afterPassword';

/*Input Element에 유효성검사 추가*/
var afterPasswordInput = document.querySelector('#afterPassword');
afterPasswordInput.addEventListener("keyup", (e) => {
    validationInput(afterPasswordInputId, passwordRegexExp);
})

function onSubmitChange(){
    const changeUserPasswordForm = document.querySelector('#changeUserPasswordForm');

    // 유효성 검사
    validationInput(afterPasswordInputId, passwordRegexExp);
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