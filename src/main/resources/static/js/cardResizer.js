$(document).ready(
    function(){
        let allWidth = $('.timetable-container').width();
        let allHeight = $('.timetable-container').height();

        let cardWidth = 300 > allWidth / 5 ? 300 : allWidth / 5;

        let cardHeight = 500 < allHeight / 3 ? allHeight / 3 : 500;

        $('.card').width(cardWidth);
        $('.card').height(cardHeight);
    }
);