// Regex Expression 선언

// 아이디
// 4~12자 이내, 영문필수, 숫자 사용가능
const userIdRegexExp = "^(?=[a-zA-Z0-9]{4,12}$)(?=.*[a-zA-Z]).*$";

// 비밀번호
// 8자 이상, 영문,숫자,기호 사용가능, 영문 대문자 또는 특수기호 1개이상 포함필수
const passwordRegexExp = "^(?=.*[A-Z!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,}$";

// 닉네임
// 2~12자 이내, 영문, 숫자, 한글 사용가능
const nicknameRegexExp = "^(?=.*[a-zA-Z0-9가-힣])[a-zA-Z0-9가-힣]{2,12}$";

// 이메일
// 가운데 @ 문자, @문자 뒤로 .com, .net 등 최상위 도메인 검사
const emailRegexExp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$";

// 비밀번호 Form Input 표시여부 이미지 버튼 클릭
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

// FormInput validation 상태 검증, 변경
function validationInput(inputId, regex){
    var inputElement = document.querySelector(inputId);

    const match = tryMatchInput(inputId, regex);

    inputElement.classList.remove('is-valid');
    inputElement.classList.remove('is-invalid');
    // Valid
    if(match){
        inputElement.classList.add('is-valid');
        // Invalid
    } else {
        inputElement.classList.add('is-invalid');
    }
}

// FormInput validation 체크
function tryMatchInput(inputId, regex){
    var inputElement = document.querySelector(inputId);

    // 이메일 유효성 검사 Regex
    const regexPattern = new RegExp(regex);
    const match = regexPattern.test(inputElement.value);
    return match;
}