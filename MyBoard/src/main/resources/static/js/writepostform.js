let editorRef;
let imgAddedCount = 1;
let imageSources = new Array();

let nowSubmitting = false;
let editMode;

// ClassicEditor 기본 설정
ClassicEditor
    .create( document.querySelector( '#editor' ), {
        simpleUpload: {
             uploadUrl: makeURL('/ftp/userimage')
        }
    })
    .then( newEditor => {
        onEditorCreated(newEditor);
    } )
    .catch( error => {
        console.error( error );
    } );

// writepostform init
function init(editMode, content){
    this.editMode = editMode;

    editorRef.model.schema.extend('imageBlock', {allowAttributes: 'imageID'} );

    // 이미지 관련 공통 처리
    // edtior Data getData() 실행하여 downcastDispatcher 실행되는 시점에 img 관련 처리
    editorRef.data.downcastDispatcher.on('attribute:imageID:imageBlock', (evt, data, conversionApi) => {
        // imageBlock model element view element로 전환(imageBlock -> figure)
        const figureElement = conversionApi.mapper.toViewElement(data.item);

        // figure의 자식 img 태그 탐색
        const imageElement = figureElement.getChild(0);

        // img에 attribute 추가
        conversionApi.writer.setAttribute('id', data.attributeNewValue, imageElement);

        // img에 class 추가
        conversionApi.writer.addClass('post_image', imageElement);
    });

    // editor Data setData() 실행하여 upcastDispatcher 실행되는 시점에 img 관련 처리
    editorRef.data.upcastDispatcher.on('element:img', (evt, data, conversionApi) => {
        if(data.modelCursor.stickiness != 'toNone'){
            const imageID = data.viewItem.getAttribute('id');
            for(const item of data.modelRange.getItems()){
                conversionApi.writer.setAttribute('imageID', imageID, item);
            }
        }
        console.log('upcast'); 
    });

    // 이미지 업로드 시 이벤트
    const imageUploadEditing = editorRef.plugins.get('ImageUploadEditing');
    imageUploadEditing.on('uploadComplete', (evt, { data, imageElement} ) => {
        // 이미지 업로드 시 EditView에 attribute, class 추가
        editorRef.editing.view.change( writer => {
            //
            const viewImage = editorRef.editing.mapper.toViewElement(imageElement).getChild(0);
            writer.setAttribute( 'imageID', imgAddedCount, viewImage );
            writer.addClass('post_image', viewImage);
        } );

        // 이미지 업로드 시 Model에 attribute 추가
        editorRef.model.change( writer => {
            writer.setAttribute('imageID', imgAddedCount, imageElement);
        });
        imgAddedCount += 1;
    })

    // 이미지 변경(이미지 업로드 후 undo 또는 삽입 한 이미지 에디터에서 삭제)
    // 신규 작성 중에 추가된 이미지인지 기등록된 이미지인지에 따라 다르게 처리
    // 기등록 이미지일 경우 이 시점에서 제거하지 않고 삭제 대상 array를 만든다.
    // 최종 수정 완료 시점에 array 전달하여 서버에서 FTP 및 DB 수정하도록 처리
    editorRef.model.document.on('change:data', (e, batch) => {
        var changedArray = e.source.differ._cachedChanges;
        for(var changedData of changedArray){
            if(changedData.name == 'imageBlock' && changedData.type == 'remove'){
                var imageSource = changedData.attributes.get('src');
                if(typeof imageSource != 'undefined'){  // FTP 이미지 업로드 실패 시 imageSource가 undefined일 수 있다. 이런경우는 FTP에 올라간 데이터가 없기 때문에 삭제 X
                    var imageFileName = getFileNameByImageSrcURL(imageSource);

                    sendFTPDeleteImage(imageFileName);
                }
            }
        }
        console.log('data changed');
    })

    // 수정 모드일 경우 editor에 기등록된 게시글 content binding
    if(editMode == 'modify')
        initData(content);
}

// writepostform 수정 일 경우 editor에 binding 할 게시글 내용 전송
function initData(postContent){
    editorRef.setData(postContent);
}

// ClassicEditor ref 생성 시 이미지 업로드 등 관련옵션 설정
function onEditorCreated (newEditor){
    editorRef = newEditor;
}

function getFileNameByImageSrcURL(imageSrcURL){
    var srcParamIdx = imageSrcURL.lastIndexOf('=');
    var fileName = imageSrcURL.substring(srcParamIdx + 1);
    return fileName;
}

function getImageSource() {
    // 이미지 src 얻어냄
    const images = document.querySelectorAll('.post_image');
    for(let i =0; i < images.length; ++i) {
        // 이미지 src 에서 파일이름 추출, 확장자 때고 id로 저장
        var imageSrcURL = images[i].getAttribute('src');
        var fileName = getFileNameByImageSrcURL(imageSrcURL);

        var extIdx = fileName.lastIndexOf('.');
        var fileID = fileName.substring(0, extIdx);

        imageSources[i] = {
            // DTO 구조에 맞춰서 데이터 만듦
            // DTO의 ID는 DB Insert 후 채번된다 이 시점에서는 temp 값 전달
            id: -1,
            postID: -1,
            // FileID
            fileID: fileID,
            fileName: fileName
        };
    }
}

// 게시글 작성 서버로 post
function onSubmitPost() {
    getImageSource();
    const writePostForm = document.querySelector('#writePostForm');
    const sendData = {
        title: writePostForm.elements['title'].value,
        content: editorRef.getData(),
        imageSource: imageSources,
        boardID: writePostForm.elements['boardID'].value,
        writerID: writePostForm.elements['writerID'].value,
    };
    const url = makeURL('/post');
    fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(sendData)
    })
    .then((response) => {
        console.log('onSubmitPost 성공 : ', response);
        nowSubmitting = true;
        if(response.redirected)
            window.location.href = response.url;
    })
    .catch((error) => {
        console.log('onSubmitPost 실패 : ', error);
    });
}

// 게시글 수정 서버로 post
function onModifyPost(){
    var content = editorRef.getData();
    console.log('수정');
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

    getImageSource();
    for(var imageSource of imageSources) {
        sendFTPDeleteImage(imageSource.fileName);
    }
    return 0;
});
