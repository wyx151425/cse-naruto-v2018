const main = new Vue({
    el: "#main",
    data: {
        isDisabled: false,
        action: "保存",
        material: {}
    },
    methods: {
        setMaterial: function (material) {
            this.material = material;
        },
        saveMaterialInfo: function () {
            this.isDisabled = true;
            axios.put(requestContext + "api/materials", main.material)
                .then(function (response) {
                    main.isDisabled = false;
                    if (200 === response.data.statusCode) {
                        popoverSpace.append("保存成功", true);
                    } else {
                        popoverSpace.append("保存失败", false);
                    }
                }).catch(function () {
                main.isDisabled = false;
                popoverSpace.append("服务器访问失败", false);
            });
        }
    },
    mounted: function () {
        let url = window.location;
        let code = getUrlParam(url, "materialCode");
        axios.get(requestContext + "api/materials/" + code)
            .then(function (response) {
                let statusCode = response.data.statusCode;
                if (200 === statusCode) {
                    let material = response.data.data;
                    if (null !== material) {
                        main.setMaterial(material);
                    } else {
                        popoverSpace.append("数据获取失败", false);
                    }
                } else {
                    let message = getMessage(statusCode);
                    popoverSpace.append(message, false);
                }
                loadModal.loadCallback();
            }).catch(function () {
            popoverSpace.append("服务器访问失败", false);
            loadModal.loadCallback();
        });
    }
});