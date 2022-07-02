window.onload = function(){
    var linkElement = document.getElementById("postLink");
    var curURL = window.location.toString();
    linkElement.href = curURL;
    linkElement.innerText = curURL;
}