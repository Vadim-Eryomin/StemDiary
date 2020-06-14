let calendar = React.createElement('div',
    {'key': idCounter++, 'className': 'calendar-container'},
    [
        React.createElement('div', {'key': idCounter++,
        'style' : {'marginLeft': 'auto', 'marginRight': 'auto'}}, [
            React.createElement('button', {'key': idCounter++, 'id': 'dayMinus', 'className': 'btn btn-outline-primary', 'type': 'button'}, '<'),
            React.createElement('label', {'key': idCounter++, 'id': 'day'}, '01'),
            React.createElement('button', {'key': idCounter++, 'id': 'dayPlus', 'className': 'btn btn-outline-primary', 'type': 'button'}, '>'),
            React.createElement('br', {'key': idCounter++}, null),

            React.createElement('button', {'key': idCounter++, 'id': 'monthMinus', 'className': 'btn btn-outline-primary', 'type': 'button'}, '<'),
            React.createElement('label', {'key': idCounter++, 'id': 'month'}, '01'),
            React.createElement('button', {'key': idCounter++, 'id': 'monthPlus', 'className': 'btn btn-outline-primary', 'type': 'button'}, '>'),
            React.createElement('br', {'key': idCounter++}, null),

            React.createElement('button', {'key': idCounter++, 'id': 'yearMinus', 'className': 'btn btn-outline-primary', 'type': 'button'}, '<'),
            React.createElement('label', {'key': idCounter++, 'id': 'year'}, '20'),
            React.createElement('button', {'key': idCounter++, 'id': 'yearPlus', 'className': 'btn btn-outline-primary', 'type': 'button'}, '>'),
            React.createElement('br', {'key': idCounter++}, null),

            React.createElement('button', {'key': idCounter++, 'id': 'hourMinus', 'className': 'btn btn-outline-primary', 'type': 'button'}, '<'),
            React.createElement('label', {'key': idCounter++, 'id': 'hour'}, '00'),
            React.createElement('button', {'key': idCounter++, 'id': 'hourPlus', 'className': 'btn btn-outline-primary', 'type': 'button'}, '>'),
            React.createElement('br', {'key': idCounter++}, null),

            React.createElement('button', {'key': idCounter++, 'id': 'minuteMinus', 'className': 'btn btn-outline-primary', 'type': 'button'}, '<'),
            React.createElement('label', {'key': idCounter++, 'id': 'minute'}, '00'),
            React.createElement('button', {'key': idCounter++, 'id': 'minutePlus', 'className': 'btn btn-outline-primary', 'type': 'button'}, '>'),
            React.createElement('br', {'key': idCounter++}, null),
        ])

    ]
);

ReactDOM.render(calendar, document.getElementById('calendar'));

let setDate = function(labelId, count){
    let label = $('#'+labelId);
    let value = parseInt($.trim(label.text())) + count;
    value = labelId === 'day' ? value % 31 + Math.trunc(value / 31) : value;
    value = labelId === 'month' ? value % 12 + Math.trunc(value / 12) : value;
    value = labelId === 'hour' ? value % 24 : value;
    value = labelId === 'minute' ? value % 60 : value;
    value = value >= 10 ? value : '0' + value;
    label.text(value);
    value = labelId === 'year' ? '20' + (value % 100) : value;
    return value;
}

$(document).ready(
    function(){
        $('#calendar').slideToggle(0);
        $('#calendar-a').click(
            function(){
                $('#calendar').slideToggle();
            }
        )
    }
)


$('#dayMinus').click(
    function(){
        let date = setDate('year', 0) + '-' + setDate('month', 0) + '-' + setDate('day', -1) + 'T' + setDate('hour', 0) + ':' + setDate('minute', 0);
        $('#time').val(date);
    }
);
$('#dayPlus').click(
    function(){
        let date = setDate('year', 0) + '-' + setDate('month', 0) + '-' + setDate('day', 1) + 'T' + setDate('hour', 0) + ':' + setDate('minute', 0);
        $('#time').val(date);
    }
);
$('#monthMinus').click(
    function(){
        let date = setDate('year', 0) + '-' + setDate('month', -1) + '-' + setDate('day', 0) + 'T' + setDate('hour', 0) + ':' + setDate('minute', 0);
        $('#time').val(date);
    }
);
$('#monthPlus').click(
    function(){
        let date = setDate('year', 0) + '-' + setDate('month', 1) + '-' + setDate('day', 0) + 'T' + setDate('hour', 0) + ':' + setDate('minute', 0);
        $('#time').val(date);
    }
);
$('#yearMinus').click(
    function(){
        let date = setDate('year', -1) + '-' + setDate('month', 0) + '-' + setDate('day', 0) + 'T' + setDate('hour', 0) + ':' + setDate('minute', 0);
        $('#time').val(date);
    }
);
$('#yearPlus').click(
    function(){
        let date = setDate('year', 1) + '-' + setDate('month', 0) + '-' + setDate('day', 0) + 'T' + setDate('hour', 0) + ':' + setDate('minute', 0);
        $('#time').val(date);
    }
);
$('#hourMinus').click(
    function(){
        let date = setDate('year', 0) + '-' + setDate('month', 0) + '-' + setDate('day', 0) + 'T' + setDate('hour', -1) + ':' + setDate('minute', 0);
        $('#time').val(date);
    }
);
$('#hourPlus').click(
    function(){
        let date = setDate('year', 0) + '-' + setDate('month', 0) + '-' + setDate('day', 0) + 'T' + setDate('hour', 1) + ':' + setDate('minute', 0);
        $('#time').val(date);
    }
);
$('#minuteMinus').click(
    function(){
        let date = setDate('year', 0) + '-' + setDate('month', 0) + '-' + setDate('day', 0) + 'T' + setDate('hour', 0) + ':' + setDate('minute', -1);
        $('#time').val(date);
    }
);
$('#minutePlus').click(
    function(){
        let date = setDate('year', 0) + '-' + setDate('month', 0) + '-' + setDate('day', 0) + 'T' + setDate('hour', 0) + ':' + setDate('minute', 1);
        $('#time').val(date);
    }
);