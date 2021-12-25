function verify() {
    let code = 1;
    let username = $.cookie("username");
    let token = $.cookie("token");
    if (username!=="null"&&token!=="null") {
        $.ajax({
            url: "/api/verify",
            method: "post",
            async:false,
            data: {"username": username, "token": token},
            success: function (res) {
                code=200
                if (res.content==="1"){
                    code=201
                }
                if (res.code !== 200) {
                    console.log(res)
                    $.cookie("username", null)
                    $.cookie("token", null);

                    code=res.code

                }

            }

        })
        return code;
    }else {
        return -1;
    }
}