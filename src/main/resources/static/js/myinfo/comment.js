function searchComment(pageNumber){
    var protocol = window.location.protocol;
    var domain = window.location.host;
    var path = '/mypage/comment' + '?pageNumber=' + pageNumber;
    var completeURL = protocol + '//' + domain + path;

    window.location.href = completeURL;
}