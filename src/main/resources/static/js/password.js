$(document).ready(function(){
    $('#password-editor').slideToggle(0);
    $('#password-edit').click(function(){
        $('#password-editor').slideToggle("medium", "linear");
    });
})