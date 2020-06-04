let allow = React.createElement('div', {'key': idCounter++, 'className': 'allow'}, [
    React.createElement('img', {'key': idCounter++, 'src': 'image/icons8-галочка-100.png', 'width': '100px',
    'style': {'marginLeft': 'auto', 'marginRight': 'auto'}}),
    React.createElement('p', {'key': idCounter++, 'style':{'textAlign':'center', 'margin': '10px'}}, 'Вы успешно купили данный товар!'),
    React.createElement('p', {'key': idCounter++, 'style':{'textAlign':'center', 'margin': '10px'}}, 'Он добавлен в вашу корзину!')
]);
let declineMoney = React.createElement('div', {'key': idCounter++, 'className': 'allow'}, [
    React.createElement('img', {'key': idCounter++, 'src': 'image/icons8-нет-входа-100.png', 'width': '100px',
    'style': {'marginLeft': 'auto', 'marginRight': 'auto'}}),
    React.createElement('p', {'key': idCounter++, 'style':{'textAlign':'center', 'margin': '10px'}}, 'Упс...'),
    React.createElement('p', {'key': idCounter++, 'style':{'textAlign':'center', 'margin': '10px'}}, 'У вас недостаточно стемкоинов!')
]);
let declineProduct = React.createElement('div', {'key': idCounter++, 'className': 'allow'}, [
    React.createElement('img', {'key': idCounter++, 'src': 'image/icons8-нет-входа-100.png', 'width': '100px',
    'style': {'marginLeft': 'auto', 'marginRight': 'auto'}}),
    React.createElement('p', {'key': idCounter++, 'style':{'textAlign':'center', 'margin': '10px'}}, 'Упс...'),
    React.createElement('p', {'key': idCounter++, 'style':{'textAlign':'center', 'margin': '10px'}}, 'Товара нет в наличии!'),
    React.createElement('p', {'key': idCounter++, 'style':{'textAlign':'center', 'margin': '10px'}}, 'Попробуйте позже!')
]);

$(".click-check").click(
    function(){
        let text = $(this).parent().parent().children(".counter").text();
        let count = parseInt($.trim(text).split(" ")[$.trim(text).split(" ").length - 1]);
        let coins = parseInt($(".coins").text().split(" ")[$(".coins").text().split(" ").length - 2]);

        if(count > 0){
            if(coins >= parseInt($(this).text())){
                ReactDOM.render(allow, document.getElementById('alert-container'));
                setTimeout(()=>{document.location.replace("/shop?buy=" + $(this).parent().children("#id").val())}, 1500);

            }
            else{
                ReactDOM.render(declineMoney, document.getElementById('alert-container'));
                setTimeout(()=>{ReactDOM.unmountComponentAtNode(document.getElementById('alert-container'))}, 1500);
            }
        }
        else{
            ReactDOM.render(declineProduct, document.getElementById('alert-container'));
            setTimeout(()=>{ReactDOM.unmountComponentAtNode(document.getElementById('alert-container'))}, 1500);
        }
    }
)