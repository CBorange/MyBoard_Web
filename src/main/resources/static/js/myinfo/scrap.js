let curScrapId = 0;
let curScrapRemark = "";
let curPostId = 0;
let curPostTitle = 0;

function searchScrap(pageNumber){
    var protocol = window.location.protocol;
    var domain = window.location.host;
    var path = '/mypage/scrap' + '?pageNumber=' + pageNumber;
    var completeURL = protocol + '//' + domain + path;

    window.location.href = completeURL;
}

function onClickShowScrapModal(scrapId, scrapRemark, postId, postTitle){
    // Modal에 표시할 Scrap, Post 정보 저장
    curScrapId = scrapId;
    curScrapRemark = scrapRemark;
    curPostId = postId;
    curPostTitle = postTitle;

    // 모달 화면에 표시
    var scrapPostTitle = document.getElementById("scrapmodal_post_title");
    scrapPostTitle.innerText = curPostTitle;

    var scrapRemarkTextArea = document.getElementById("scrap_remark_textarea");
    scrapRemarkTextArea.value = curScrapRemark;

    var myModal = new bootstrap.Modal(document.getElementById('scrapModal'), null);
    myModal.show();
}

function onClickModifyScrap(userId){
    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');

    var tokenInfo = getCSRFToken();
    if(tokenInfo != null){
        headers.append(tokenInfo.header, tokenInfo.token);
    }

    // 스크랩 메모 수정사항 획득
    var scrapRemarkTextArea = document.getElementById("scrap_remark_textarea");
    curScrapRemark = scrapRemarkTextArea.value;

    const url = makeURL('/post/scrap/' + curScrapId);
    fetch(url,{
        method: 'POST',
        headers: headers,
        body: 'newRemark=' + curScrapRemark
    })
    .then((response) => {
        if(!response.ok){
            response.text()
                .then((text) => {
                    alert(text);
                })
        } else{
            alert("스크랩이 수정 되었습니다.");
        }
        // refresh
        window.location.reload();
    })
    .catch((error) => {
        console.log('onClickModifyScrap 실패 : ', error);
        alert(error);
    })
}

function onClickDeleteScrap(scrapId){
    var isOk = confirm("스크랩을 삭제하시겠습니까?");
    if(!isOk)
        return;

    var headers = new Headers();
    var tokenInfo = getCSRFToken();
    if(tokenInfo != null){
        headers.append(tokenInfo.header, tokenInfo.token);
    }

    const url = makeURL('/post/scrap/' + scrapId);
    fetch(url,{
        method: 'DELETE',
        headers: headers
    })
    .then((response) => {
        if(!response.ok){
            response.text()
                .then((text) => {
                    alert(text);
                })
        } else{
            alert("스크랩이 삭제 되었습니다.");
        }
        // refresh
        window.location.reload();
    })
    .catch((error) => {
        console.log('onClickDeleteScrap 실패 : ', error);
        alert(error);
    })
}

