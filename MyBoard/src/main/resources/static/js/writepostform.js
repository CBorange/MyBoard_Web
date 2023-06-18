let editorRef;
let nowSubmitting = false;
let editMode;

// 수정 모드일 경우 editor에 content 로드할 때 DB에 저장된 content에서 img 태그만 발라내어 여기에 저장해둔다(원본)
// 마지막에 변경된 내용 저장할 때 또는 창이 닫힐 때 이 원본 데이터와 비교하여 삭제된 이미지가 무엇인지 판별한다.
let loadedContentImages = new Array();

// ClassicEditor 기본 설정
ClassicEditor
    .create( document.querySelector( '#editor' ), {
        simpleUpload: {
             uploadUrl: makeURL('/ftp/userimage')
        }
    })
    .then( newEditor => {
        editorRef = newEditor;
    } )
    .catch( error => {
        console.error( error );
    } );

// writepostform init
function init(editMode, content){
    this.editMode = editMode;

    // 이미지 관련 공통 처리
    // edtior Data getData() 실행하여 downcastDispatcher 실행되는 시점에 img 관련 처리
    editorRef.data.downcastDispatcher.on('insert:imageBlock', (evt, data, conversionApi) => {
        // imageBlock model element view element로 전환(imageBlock -> figure)
        const figureElement = conversionApi.mapper.toViewElement(data.item);

        // figure의 자식 img 태그 탐색
        const imageElement = figureElement.getChild(0);

        // img에 class 추가
        conversionApi.writer.addClass('post_image', imageElement);
    });

    // 수정 모드일 경우 editor에 기등록된 게시글 content binding
    if(editMode == 'modify'){
        loadedContentImages = Array.from( new DOMParser().parseFromString( content, 'text/html' )
        .querySelectorAll( 'img' ) )
        .map( img => img.getAttribute( 'src' ) );

        initData(content);
    }
        
}

// writepostform 수정 일 경우 editor에 binding 할 게시글 내용 전송
function initData(postContent){
    editorRef.setData(postContent);
}

function getFileNameByImageSrcURL(imageSrcURL){
    var srcParamIdx = imageSrcURL.lastIndexOf('=');
    var fileName = imageSrcURL.substring(srcParamIdx + 1);
    return fileName;
}

// 아직 DB에 저장되지 않은(작성중인) 이미지 src 얻어냄
function getUnsubmittedImages() {
    let resultIdx = 0;
    var result = new Array();

    // editor에서 getData 실행하여 img만 얻어내어 src를 발라낸다.
    const imageSources = Array.from( new DOMParser().parseFromString( editorRef.getData(), 'text/html' )
        .querySelectorAll( 'img' ) )
        .map( img => img.getAttribute( 'src' ) );

    // 현재 editor 의 getData(imageSources) -> 원본데이터 를 비교하여 추가된 이미지를 판별한다.
    for(let orgIdx = 0; orgIdx < imageSources.length; ++ orgIdx){
        var orgFileName = getFileNameByImageSrcURL(imageSources[orgIdx]);
        var orgExtIdx = orgFileName.lastIndexOf('.');
        var orgFileID = orgFileName.substring(0, orgExtIdx);

        let editorExistsInOrg = false;
        for(let i = 0; i < loadedContentImages.length;++i){
            var targetFileName = getFileNameByImageSrcURL(loadedContentImages[i]);
            var targetExtIdx = targetFileName.lastIndexOf('.');
            var targetFileID = targetFileName.substring(0, targetExtIdx);

            if(orgFileID == targetFileID){
                editorExistsInOrg = true;
                break;
            }
        }
        if(!editorExistsInOrg) {
            result[resultIdx] = {
                fileID: orgFileID,
                fileName: orgFileName,
                state: 'insert'
            };
        }
    }



    return result;
}

// 삭제된 이미지 src 얻어냄
function getDeletedImages(){
    let resultIdx = 0;
    var result = new Array();

    // editor에서 getData 실행하여 img만 얻어내어 src를 발라낸다.
    const imageSources = Array.from( new DOMParser().parseFromString( editorRef.getData(), 'text/html' )
        .querySelectorAll( 'img' ) )
        .map( img => img.getAttribute( 'src' ) );

    // 원본 데이터 -> 현재 editor 의 getData(imageSources) 를 비교하여 삭제된 이미지를 판별한다.
    for(let orgIdx = 0; orgIdx < loadedContentImages.length; ++ orgIdx){
        var orgFileName = getFileNameByImageSrcURL(loadedContentImages[orgIdx]);
        var orgExtIdx = orgFileName.lastIndexOf('.');
        var orgFileID = orgFileName.substring(0, orgExtIdx);

        let orgExistsInEditor = false;
        for(let i = 0; i < imageSources.length;++i){
            var targetFileName = getFileNameByImageSrcURL(imageSources[i]);
            var targetExtIdx = targetFileName.lastIndexOf('.');
            var targetFileID = targetFileName.substring(0, targetExtIdx);

            if(orgFileID == targetFileID){
                orgExistsInEditor = true;
                break;
            }
        }
        if(!orgExistsInEditor) {
            result[resultIdx] = {
                fileID: orgFileID,
                fileName: orgFileName,
                state: 'delete'
            };
        }
    }

    return result;
}

// 게시글 생성 또는 수정 POST API 호출
// submitState : 신규 게시글 등록(insert), 기존 게시글 수정(update)로 들어옴
function onSubmitPost(submitState) {

    // submitPost 호출할 때 서버에 넘기는 Image Delta 데이터는 일차적으로
    // insert 데이터와 delete 데이터로 분리하여 전달한다.
    // unsubmitted_image class가 등록된 이미지(unsubmittedImage)는 insert로(수정 또는 생성 시 신규로 삽입된 이미지임)
    // editor에서 remove event 발생 시 caching한 데이터는 delete로(기등록된 데이터, 신규데이터 구분 없음 전부 전달하고
    // DB에서 구분 없이 전부 삭제 쿼리 실행함, 유효하면 삭제되고 아니면 무시될것임)
    var unsubmittedImages = getUnsubmittedImages();
    var deletedImages = getDeletedImages();
    var finalImageSources = new Array();
    for(let i = 0; i < unsubmittedImages.length; ++i){
        finalImageSources.push(unsubmittedImages[i]);
    }
    for(let i = 0; i < deletedImages.length; ++i){
        finalImageSources.push(deletedImages[i]);
    }

    const writePostForm = document.querySelector('#writePostForm');
    const sendData = {
        state : submitState,
        title: writePostForm.elements['title'].value,
        content: editorRef.getData(),
        imageSource: finalImageSources,
        postId: writePostForm.elements['postID'].value,
        boardId: writePostForm.elements['boardID'].value,
        writerId: writePostForm.elements['writerID'].value,
        writerNickname: writePostForm.elements['writerNickname'].value,
    };
    const url = makeURL('/post');
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(sendData)
    })
    .then((response) => {
        console.log('onSubmitPost POST API 전송결과 : ', response);
        nowSubmitting = true;
        if(response.redirected)
            window.location.href = response.url;
    })
    .catch((error) => {
        console.log('onSubmitPost 실패 : ', error);
    });
}

// FTP 이미지 삭제 Reqeust 전송
function sendFTPDeleteImage(targetImageFileName){
    const url = makeURL('/ftp/userimage/' + targetImageFileName);
    fetch(url, {
        method: 'DELETE',
        keepalive: true
    })
    .then((response) => {
        console.log('이미지 삭제 성공 : ', response);
    })
    .catch((error) => {
        console.log('이미지 삭제 실패 : ', error);
    });
}

// 브라우저 관련 event 처리
// 새로고침, 닫기, 뒤로가기 등 작성중 취소 이벤트 처리(FTP 파일제거)
window.addEventListener('beforeunload', function(e){
    if(nowSubmitting)
        return 0;

    var imageSources = getUnsubmittedImages();
    for(var imageSource of imageSources) {
        sendFTPDeleteImage(imageSource.fileName);
    }
    return 0;
});
