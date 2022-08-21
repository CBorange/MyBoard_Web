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
    // editor Data 얻어냄
    const editorData = editorRef.getData();
}
