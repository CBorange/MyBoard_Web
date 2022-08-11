let editor;

ClassicEditor
    .create( document.querySelector( '#editor' ))
    .then( newEditor => {
        editor = newEditor;
    } )
    .catch( error => {
        console.error( error );
    } );

function onSubmitPost() {
    // editor Data 얻어냄
    const editorData = editor.getData();
}
