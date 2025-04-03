function notDeleteReview(){
    window.location.href = "/gymclass";
}

function DeleteReview(id,action){
    window.location.href = "/gymclass/" + id + "/delete/" + action;
}