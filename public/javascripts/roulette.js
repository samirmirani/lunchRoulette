$( document ).ready(function() {
    var lr = new LunchRoullette();
    lr.handleForm();
    lr.topNav();

});

function LunchRoullette() {
    this.topNav = function() {
        var $nav = $('#nav');
        $nav.find("li").removeClass("current");
        $nav.find("a").each(function() {
            if ($(this).attr("href") == window.location.pathname) {
                $(this).parent().addClass('current');
            }
        });
    }

    this.handleForm = function() {
    //this is not needed anymore
        $("#addUser").submit(function(e) {
            e.preventDefault();

            var name = $("#formName").val();
            var email = $("#formEmail").val();

            $.ajax({
                type: "POST",
                url: '/addUserPost',
                data: {
                    email: email
                },
                succsess: function(response) {
                    console.log(response);
                    $('.flashmessage').html(response);
                },
                error: function(response) {
                    $('.flashmessage').html(response);
                },
                dataType: 'json'
            })
        })
    }
}