let filter;

filter = React.createElement('a', {'key': idCounter++, 'className': 'btn btn-outline-success',
 'id': 'button', 'href': '/filter', 'style': {'position': 'fixed', 'bottom': '20px', 'right': '20px', 'top': ($(document).height() - 60) + 'px', 'left': ($(document).width() - 120) + 'px'}}, 'Фильтр');
ReactDOM.render(filter, document.getElementById('min-filter'));