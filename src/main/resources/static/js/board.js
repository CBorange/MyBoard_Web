function searchPost(boardId, pageNumber){
    // 검색 조건
    var searchTypeOption = document.getElementById("searchType");
    var searchType = searchTypeOption[searchTypeOption.selectedIndex].value;

    // 검색어
    var searchValue = document.getElementById("searchValue").value;

   const searchParams = new URLSearchParams(window.location.search);

   var protocol = window.location.protocol;
   var domain = window.location.host;
   var path = '/board/' + boardId + '?pageNumber=' + pageNumber + '&searchType=' + searchType + "&searchValue=" + searchValue;
   var completeURL = protocol + '//' + domain + path;

   window.location.href = completeURL;
}