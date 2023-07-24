function makeURL(resource) {
    var generatedURL = host + resource;
    return generatedURL;
}

function getCSRFToken(){
    var csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    var csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    
    const result = {
        header: csrfHeader,
        token: csrfToken
    };
    return result;
}