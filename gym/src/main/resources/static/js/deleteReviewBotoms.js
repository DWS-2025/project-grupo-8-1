function notDeleteReview(){
    window.location.href = "/review";
}

function DeleteReview(id,action){
    window.location.href = "/review/" + id + "/delete/" + action;
}