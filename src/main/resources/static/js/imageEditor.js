$(document).ready(
    function(){
        let needWidth = $('.news').width()/1.5;
        $('.news-image').width(needWidth);

        let needWidthForPage = $(document).width()/10 > 200 ? $(document).width()/10 : 200;
        $('.timetable-image').width(needWidthForPage);
        $('.resize-image').width(needWidthForPage);
    }
);