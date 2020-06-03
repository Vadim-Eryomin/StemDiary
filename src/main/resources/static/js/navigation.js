let idCounter = 0;

let admin = document.getElementById('root').getAttribute('admin') === 'true';
document.getElementById('root').removeAttribute('admin');

let navigationColor = document.getElementById('root').getAttribute('color');
document.getElementById('root').removeAttribute('color');

let h;
let doButtonMinMenuAnimation = true;
let minMenuAnimateInOutCounter = 0;
let animationDelay = 200;
let windowWidth = document.body.offsetWidth;

let styleNavigation = {backgroundColor: '#' + navigationColor };
if(document.body.offsetWidth >= 800){
    h = React.createElement('div', {'key': idCounter++, 'className': 'navigation', 'style': styleNavigation}, [
        React.createElement('div', {'key': idCounter++, 'className': 'navigation-bar', 'style': styleNavigation},
            [
                React.createElement('a', {'key': idCounter++, 'href':'/profile'}, [
                    React.createElement('img', {'key': idCounter++, 'src': 'image/stem_top_left_logo.png', 'className':"navigation-image", 'style':styleNavigation})
                ]),
                React.createElement('a', {'key': idCounter++, 'href': '/profile', 'className':"navigation-text"}, 'Профиль'),
                React.createElement('a', {'key': idCounter++, 'href': '/news', 'className':"navigation-text"}, 'Новости'),
                React.createElement('a', {'key': idCounter++, 'href': '/shop', 'className':"navigation-text"}, 'Магазин'),
                React.createElement('a', {'key': idCounter++, 'href': '/timetable', 'className':"navigation-text"}, 'Расписание'),
                admin? React.createElement('a', {'key': idCounter++, 'href': '/admin', 'className':"navigation-text"}, 'Панель Администратора') : null,
                React.createElement('a', {'key': idCounter++, 'href': '/login', 'className':"navigation-text"}, 'Выйти'),
            ])
    ]);
}
else{
    h = React.createElement('div', {'key': idCounter++, 'className': 'navigation-min', 'style': styleNavigation}, [
        React.createElement('a', {'key': idCounter++, 'href':'/profile'}, [
            React.createElement('img', {'key': idCounter++, 'src': 'image/stem_top_left_logo.png', 'className':"navigation-image", 'style': styleNavigation})
        ]),
        React.createElement('div', {'key': idCounter++, 'className': 'navigation-min', 'style': styleNavigation}, [
            React.createElement('button', {'key': idCounter++, 'id':'min-menu-button', 'style': styleNavigation}, [
                React.createElement('i', {'key': idCounter++, 'className':'fa fa-bars', 'style': styleNavigation})
            ]),
            React.createElement('div', {'key': idCounter++, 'id': 'menu'}, [
                React.createElement('a', {'key': idCounter++, 'href': '/profile', 'className':"min-navigation-text"}, 'Профиль'),
                React.createElement('a', {'key': idCounter++, 'href': '/news', 'className':"min-navigation-text"}, 'Новости'),
                React.createElement('a', {'key': idCounter++, 'href': '/shop', 'className':"min-navigation-text"}, 'Магазин'),
                React.createElement('a', {'key': idCounter++, 'href': '/timetable', 'className':"min-navigation-text"}, 'Расписание'),
                admin? React.createElement('a', {'key': idCounter++, 'href': '/admin', 'className':"min-navigation-text"}, 'Панель Администратора') : null,
                React.createElement('a', {'key': idCounter++, 'href': '/login', 'className':"min-navigation-text"}, 'Выйти'),
            ])
        ])
    ])
}
ReactDOM.render(h, document.getElementById('root'));

$(document).ready(
    function(){
        $('#min-menu-button').click(
            function(){
                if(doButtonMinMenuAnimation){
                    if(minMenuAnimateInOutCounter % 2 == 0){
                        $('#min-menu-button').animate({right:"250px"}, animationDelay);
                        $('#menu').animate({right:"0px"}, animationDelay);
                    }
                    else{
                        $('#min-menu-button').animate({right:"0px"}, animationDelay);
                        $('#menu').animate({right:"-300px"}, animationDelay);
                    }
                    minMenuAnimateInOutCounter++;
                    doButtonMinMenuAnimation = false;
                    setTimeout(()=>{doButtonMinMenuAnimation = true;}, animationDelay);
                }
            }
        )
    }
)
