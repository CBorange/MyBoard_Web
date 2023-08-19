// input element id 선언
const emailInputId = '#emailInput';

/*Input Element에 유효성검사 추가*/
var emailInput = document.querySelector(emailInputId);
emailInput.addEventListener("keyup", (e) => {
    validationInput(emailInputId, emailRegexExp);
});

function onClickSend(){
    // 유효성 검사
    validationInput(emailInputId, emailRegexExp);
    var inputList = document.querySelectorAll('#sendForm input');
    for(inputTag of inputList){
        if(inputTag.classList.contains('is-invalid')){
            alert('이메일 주소 형식이 올바르지 않습니다. 다시한번 확인 해주세요.');
            return;
        }
    }

    const sendForm = document.querySelector('#sendForm');
    var email = sendForm.elements['email'].value;

    let msg = '메일이 성공적으로 전송 되었습니다. 확인해주세요';
    const resultArea = document.querySelector('#resultArea');
    const resultMsg = document.querySelector('#resultMsg');
    const loginButton = document.querySelector('#goToLoginBtn');

    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');

    var tokenInfo = getCSRFToken();
    if(tokenInfo != null){
        headers.append(tokenInfo.header, tokenInfo.token);
    }

    resultArea.style.visibility = "visible";
    resultMsg.style.color = "black";
    resultMsg.innerText = "메일을 전송중입니다 잠시만 기다려주세요...";

    const url = makeURL('/user/find');
    fetch(url, {
        method: 'POST',
        headers: headers,
        body: 'userEmail=' + email
    })
    .then((response) => {
        console.log('onClickSend POST API 전송결과 : ', response);
        if(response.ok){
            resultMsg.style.color = "green";
            resultMsg.innerText = msg;
        }else{
            response.text().then(errorMsg => {
                msg = errorMsg;
                resultMsg.style.color = "red";
                console.log('onClickSend API 결과 : ' + errorMsg)

                resultMsg.innerText = msg;
            });
        }
    })
    .catch((error) => {
        console.log('onClickSend 실패 : ', error);
        resultMsg.style.color = "red";
        resultMsg.innerText = error;
    });
}