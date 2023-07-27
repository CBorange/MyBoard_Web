function makeURL(resource) {
    var generatedURL = host + resource;
    return generatedURL;
}

function validateHttpStatusCode(statusCode, considerSuccessFor_300 = true){
    // 성공
    if((statusCode >= 200 && statusCode <= 299) ||
       (considerSuccessFor_300 && (statusCode >= 300 && statusCode <= 399))){
        return true;
    }
    else{
        // 실패
        return false;
    }
}

function getCSRFToken(){
    var csrfTokenElement = document.querySelector('meta[name="_csrf"]');
    var csrfHeaderElement = document.querySelector('meta[name="_csrf_header"]');

    // 테스트용으로 CSRF 비활성화 시키면 CSRF 토큰 없을 수 있음
    if(csrfTokenElement != null && csrfHeaderElement != null){
        var csrfToken = csrfTokenElement.getAttribute('content');
        var csrfHeader = csrfHeaderElement.getAttribute('content');

        const result = {
            header: csrfHeader,
            token: csrfToken
        };
        return result;
    }
    return null;
}