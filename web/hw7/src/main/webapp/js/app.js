window.notify = function (message) {
    $.notify(message, {
        position: "right bottom",
        className: "success"
    });
}

window.form = function (action, q) {

    const login = q.find("input[name='login']").val();
    const password = q.find("input[name='password']").val();
    const $error = q.find(".error");

    $.ajax({
        type: "POST",
        url: "",
        dataType: "json",
        data: {
            action: action,
            login: login,
            password: password
        },
        success: function (response) {
            if (response["error"]) {
                $error.text(response["error"]);
            } else {
                location.href = response["redirect"];
            }
        }
    });
}