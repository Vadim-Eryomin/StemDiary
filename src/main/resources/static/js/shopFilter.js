let filter;
let doButtonFilterAnimation = true;
let filterAnimateInOutCounter = 0;
let filterAnimationDelay = 200;
let isOpen = false;

if(document.body.offsetWidth <= 800){
    filter = React.createElement('button', {'key': idCounter++, 'className': 'btn btn-outline-success', 'id': 'button'}, 'Фильтр')
}
ReactDOM.render(filter, document.getElementById('min-filter'));

$(document).ready(
    function(){
        $('#button').click(
            function(){
                if(doButtonFilterAnimation){
                    if(filterAnimateInOutCounter % 2 == 0){
                        $('#min-filter').animate({top: "200px"}, filterAnimationDelay);
                        $('.filter-container').animate({top:"48px"}, filterAnimationDelay);
                        isOpen = true;
                    }
                    else{
                        $('#min-filter').animate({top:"48px"}, filterAnimationDelay);
                        $('.filter-container').animate({top:"-200px"}, filterAnimationDelay);
                        isOpen = false;
                    }
                    $(document).trigger('scroll');
                    filterAnimateInOutCounter++;
                    doButtonFilterAnimation = false;
                    setTimeout(()=>{doButtonFilterAnimation = true;}, filterAnimationDelay);
                }
            }
        )
    }
);
$(document).scroll(
    function(){
        if($(document).scrollTop() >= 48){
            if(isOpen){
                $('#min-filter').animate({top: (210) + "px"}, 0);
                $('.filter-container').animate({top: (10) + "px"}, 0);
            }
            else{
                $('#min-filter').animate({top: (10) + "px"}, 0);
            }
        }
        else{
            if(isOpen){
                $('#min-filter').animate({top: (258 - $(document).scrollTop()) + "px"}, 0);
                $('.filter-container').animate({top: (58 - $(document).scrollTop()) + "px"}, 0);
            }
            else{
                $('#min-filter').animate({top: (58 - $(document).scrollTop()) + "px"}, 0);
            }
        }
    }
);
