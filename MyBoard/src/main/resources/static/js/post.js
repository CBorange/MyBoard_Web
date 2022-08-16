window.onload = function(){
    var linkElement = document.getElementById("postLink");
    var curURL = window.location.toString();
    linkElement.href = curURL;
    linkElement.innerText = curURL;
}

function onClickShowReplyForm(id){
    var subReplyForm = document.getElementById("subReplyForm_" + id);
    if(subReplyForm.style.display == "none")
        subReplyForm.style.display = "block";
    else
        subReplyForm.style.display = "none";
}