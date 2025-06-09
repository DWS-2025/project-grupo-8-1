function notDeleteReview(){
    window.location.href = "/gymclass";
}

function DeleteReview(id, action){
    // Crea y envía un formulario POST a /gymclass/{id}/delete con action=true
    var form = document.createElement("form");
    form.method = "POST";
    form.action = "/gymclass/" + id + "/delete";

    var input = document.createElement("input");
    input.type = "hidden";
    input.name = "action";
    input.value = action;
    form.appendChild(input);

    document.body.appendChild(form);
    form.submit();
}