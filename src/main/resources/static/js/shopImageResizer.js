$(document).ready(
    function(){
        if($(document).width() >= 800){
            let width = $(document).width() / 7;
            $(".card-img-top").width(width);
        }
        else{
            let width = $(document).width() / 4;
            $(".card-img-top").width(width);
        }
    }
);