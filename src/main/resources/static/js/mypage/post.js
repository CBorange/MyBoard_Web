function searchPost(pageNumber){
    var protocol = window.location.protocol;
    var domain = window.location.host;
    var path = '/mypage/post' + '?pageNumber=' + pageNumber;
    var completeURL = protocol + '//' + domain + path;
 
    window.location.href = completeURL;
}