let dateVal = parseInt($.trim($('.date-val').val()));
let date = new Date(dateVal);

let name = $.trim($('.name').val())

let stringDate = (date.getDate() >= 10 ? date.getDate() : '0' + date.getDate())
                + '.' + (date.getMonth() >= 10 ? date.getMonth() : '0' + date.getMonth())
                + '.' + (date.getFullYear());
$("#date-place").text(name + ' | ' + stringDate);