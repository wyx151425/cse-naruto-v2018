const container = new Vue({
    el: "#container",
    data: {
        user: {
            code: "",
            password: ""
        },
        type: "password",
        isDisabled: false,
        action: "登录"
    },
    methods: {
        removeCode: function () {
            this.user.code = "";
        },
        toggleType: function () {
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
            this.isDisabled = true;
            this.action = "正在登录";
            axios.post(requestContext + "api/users/login", this.user)
                .then(function (response) {
                    let statusCode = response.data.statusCode;
                    if (200 === statusCode) {
                        let user = response.data.data;
                        localStorage.setItem("user", JSON.stringify(user));
                        if ("admin" === user.code) {
                            window.location.href = requestContext + "admin";
                        } else if (user.roles.ROLE_GUARANTEE_EMPLOYEE) {
                            window.location.href = requestContext + "guarantee";
                        } else {
                            window.location.href = requestContext + "index";
                        }
                    } else {
                        let message = getMessage(statusCode);
                        popoverSpace.append(message, false);
                        container.loginCallback();
                    }
                }).catch(function () {
                popoverSpace.append("服务器访问失败", false);
                container.loginCallback();
            });
        },
        loginCallback: function () {
            this.action = "登录";
            this.isDisabled = false;
        }
    }
});