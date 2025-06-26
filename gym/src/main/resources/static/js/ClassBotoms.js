function edit(id){
    window.location.href = "/gymclass/" + id + "/edit";
}

function remove(id){
    var form = document.createElement("form");
    form.method = "POST";
    form.action = "/gymclass/" + id + "/delete";

    var input = document.createElement("input");
    input.type = "hidden";
    input.name = "action";
    input.value = "true";
    form.appendChild(input);

    document.body.appendChild(form);
    form.submit();
}

function downloadPDF(id){
    window.location.href = "/gymclass/file/" + id;
}

function register(id) {
    var form = document.createElement("form");
    form.method = "POST";
    form.action = "/gymclass/" + id + "/toggleUser";

    var input = document.createElement("input");
    input.type = "hidden";
    input.name = "join";
    input.value = "true";
    form.appendChild(input);

    document.body.appendChild(form);
    form.submit();
}