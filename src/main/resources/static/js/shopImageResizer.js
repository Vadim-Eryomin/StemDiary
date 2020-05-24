$(document).ready(
    function(){
        let needWidth = ($('.product-card').width() - $('.product-card-definition').width())/1.5;
        $('.product-card-image').width(needWidth);
    }
);