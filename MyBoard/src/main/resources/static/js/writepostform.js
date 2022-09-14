let editorRef;
let imgAddedCount = 1;

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

function onEditorCreated (newEditor){
    editorRef = newEditor;

    editorRef.model.schema.extend('imageBlock', {allowAttributes: 'imageID'} );

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
}

function onSubmitPost() {
    // 이미지 src 얻어냄
    const images = document.querySelectorAll('.post_image');
    var imageSources = new Array();
    for(let i =0; i < images.length; ++i) {
        var src = images[i].getAttribute('src');
        imageSources[i] = src;
    }
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
        if(response.redirected)
            window.location.href = response.url;
    })
    .catch((error) => {
        console.log('onSubmitPost 실패 : ', error);
    });
}
