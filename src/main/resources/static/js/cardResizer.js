$(document).ready(
    function(){
        let allWidth = $('.timetable-container').width();
        let allHeight = $('.timetable-container').height();

        let cardWidth = 250 > allWidth / 5 ? 250 : allWidth / 5;

        let cardHeight = 400 < allHeight / 3 ? allHeight / 3 : 400;

        $('.card').width(cardWidth);
        $('.card').height(cardHeight);
    }
);