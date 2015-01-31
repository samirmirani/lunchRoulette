$( document ).ready(function() {
    var lr = new LunchRoullette();
    lr.handleForm();
});

function LunchRoullette() {
    this.handleForm = function() {
        var disable = false;
        $("#addUser").submit(function(e) {
            e.preventDefault();
            $('.flashmessage').hide();
            if(disable) {
                return false;
            }

            var email = $("#formEmail").val();
            $.ajax({
                type: "POST",
                url: '/addUserPost',
                data: {
                    email: email
                },
                success: function(response) {
                    console.log(response);
                    $('#addUser').hide();
                    $('.flashmessage').fadeOut(40).html(response).fadeIn(450);

                    //user has completed form, disable it now.
                    disable = true;
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    $('.flashmessage').html(jqXHR.responseText).fadeIn(450);
                },
                dataType: 'json'
            })
        })
    }
}