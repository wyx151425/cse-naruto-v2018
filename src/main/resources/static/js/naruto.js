let requestContext = "http://localhost:8080/";

function getUrlParam(url, name) {
    let pattern = new RegExp("[?&]" + name + "\=([^&]+)", "g");
    let matcher = pattern.exec(url);
    let items = null;
    if (null !== matcher) {
        try {
            items = decodeURIComponent(decodeURIComponent(matcher[1]));
        } catch (e) {
            try {
                items = decodeURIComponent(matcher[1]);
            } catch (e) {
                items = matcher[1];
            }
        }
    }
    return items;
}

function getMessage(statusCode) {
    switch (statusCode) {
        case 500:
            return "服务器错误";
        case 1000:
            return "用户不存在";
        case 1001:
            return "用户已注册";
        case 1002:
            return "用户被禁用";
        case 1003:
            return "登录超时";
        case 1004:
            return "密码错误";
        case 1100:
            return "文件格式错误";
        case 1101:
            return "文件解析错误";
    }
}

Vue.component("popover", {
    props: ["prompt"],
    template: `
            <div class="popover" :class="{success: prompt.success, error: !prompt.success}">
                <span class="icon icon-ok" v-if="prompt.success"></span>
                <span class="icon icon-remove" v-if="!prompt.success"></span>
                <span>{{prompt.message}}</span>
            </div>
        `
});

const popoverSpace = new Vue({
    el: "#popoverSpace",
    data: {
        prompts: [],
        index: 0
    },
    methods: {
        append: function (message, success) {
            let prompt = {id: this.index, success: success, message: message};
            this.index++;
            this.prompts.push(prompt);
            setTimeout(function () {
                popoverSpace.prompts.shift(prompt);
            }, 5000);
        }
    }
});
const mask = new Vue({
    el: "#mask",
    data: {
        loading: true
    },
    methods: {
        loadStop: function () {
            this.loading = false;
        }
    }
});

const header = new Vue({
    el: "#header",
    data: {
        user: {}
    },
    mounted: function () {
        this.user = JSON.parse(localStorage.user);
    }
});