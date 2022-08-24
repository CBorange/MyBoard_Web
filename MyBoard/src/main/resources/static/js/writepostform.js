let editorRef;

ClassicEditor
    .create( document.querySelector( '#editor' ))
    .then( newEditor => {
        editorRef = newEditor;
    } )
    .catch( error => {
        console.error( error );
    } );

function onSubmitPost() {
    const writePostForm = document.querySelector('#writePostForm');
    
    const sendData = {
        title: writePostForm.elements['title'].value,
        content: editorRef.getData(),
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
