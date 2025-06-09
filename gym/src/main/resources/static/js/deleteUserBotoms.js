function DeleteUser(){
    var form = document.createElement("form");
    form.method = "POST";
    form.action = "/user/delete/true";

    var input = document.createElement("input");
    input.type = "hidden";
    input.name = "action";
    input.value = "true";
    form.appendChild(input);

    document.body.appendChild(form);
    form.submit();
}

function notDeleteUser(){
    window.location.href = "/home";
}