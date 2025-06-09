function notDeleteReview(){
    window.location.href = "/review";
}

function DeleteReview(id, action){
    // Crea y envía un formulario POST
    var form = document.createElement("form");
    form.method = "POST";
    form.action = "/review/" + id + "/delete";

    var input = document.createElement("input");
    input.type = "hidden";
    input.name = "action";
    input.value = action;
    form.appendChild(input);

    document.body.appendChild(form);
    form.submit();
}