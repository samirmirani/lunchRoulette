$( document ).ready(function() {
    var lr = new LunchRoullette();
    lr.handleForm();
    lr.getRouletteUsers();

});

function LunchRoullette() {
    var getUsers = function() {
        var response = function(response) {
            $('#currentUserOnRoulette').html(response);
        };

        $.ajax({
            url: "/lunch-goers",
            success: response
        });
    };

    this.getRouletteUsers = function() {
        getUsers();
    };

    this.handleForm = function() {
        $("#addUser").submit(function(e) {
            e.preventDefault();

            var name = $("#formName").val();
            var email = $("#formEmail").val();

            $.ajax({
                type: "POST",
                url: '/addUser',
                data: {
                    name: name,
                    email: email
                },
                success: function() {
                    getUsers();
                },
                dataType: 'json'
            })
        })
    }
}