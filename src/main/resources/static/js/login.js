const main = new Vue({
    el: "#main",
    data: {
        user: {
            code: "",
            password: ""
        },
        type: "password",
        signing: false,
        action: "登录"
    },
    methods: {
        removeCode: function () {
            this.user.code = "";
        },
        typeExchange: function () {
            if ("password" === this.type) {
                this.type = "text";
            } else {
                this.type = "password";
            }
        },
        login: function () {
            if ("" === this.user.code) {
                popoverSpace.append("请输入工号", false);
                return;
            }
            if ("" === this.user.password) {
                popoverSpace.append("请输入密码", false);
                return;
            }
            if (this.user.password.length < 6 || this.user.password.length > 32) {
                popoverSpace.append("请输入正确格式的密码", false);
                return;
            }
            this.signing = true;
            this.action = "正在登录";
            axios.post(requestContext + "api/users/login", this.user)
                .then(function (response) {
                    let statusCode = response.data.statusCode;
                    if (200 === statusCode) {
                        localStorage.setItem("user" ,JSON.stringify(response.data.data));
                        // popoverSpace.append("登录成功", true);
                        // main.loginResult();
                        window.location.href = requestContext + "index";
                    } else {
                        let message = getMessage(statusCode);
                        popoverSpace.append(message, false);
                        main.loginResult();
                    }
                }).catch(function (error) {
                popoverSpace.append("服务器访问失败", false);
                main.loginResult();
            });
        },
        loginResult: function () {
            this.action = "登录";
            this.signing = false;
        }
    }
});