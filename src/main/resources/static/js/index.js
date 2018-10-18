const main = new Vue({
    el: "#main",
    data: {
        loading: true,
        status: 0,
        import: false,
        exportAction: "导出",
        exporting: false,
        pageContext: {
            index: 1,
            size: 15,
            pageTotal: 0,
            dataTotal: 0,
        },
        materialList: [],
    },
    methods: {
        materialListQuery: function (index, size, status) {
            axios.get(requestContext + "api/materials?pageIndex=" + index + "&pageSize=" + size + "&status=" + status)
                .then(function (response) {
                    let statusCode = response.data.statusCode;
                    if (200 === statusCode) {
                        let data = response.data.data;
                        main.pageContext.index = data.index;
                        main.pageContext.pageTotal = data.pageTotal;
                        main.pageContext.dataTotal = data.dataTotal;
                        main.materialList = data.data;
                    } else {
                        popoverSpace.append("数据获取失败", false);
                    }
                }).catch(function () {
                popoverSpace.append("服务器访问失败", false);
            });
        },
        previousPage: function () {
            if (this.pageContext.index > 1) {
                this.materialListQuery(this.pageContext.index - 1, this.pageContext.size, this.status);
            }
        },
        nextPage: function () {
            if (this.pageContext.index < this.pageContext.pageTotal) {
                this.materialListQuery(this.pageContext.index + 1, this.pageContext.size, this.status);
            }
        },
        importModal: function () {
            modal.importModal();
        },
        statusQuery: function () {
            this.materialListQuery(1, 15, this.status);
        },
        exportFile: function () {
            this.exportAction = "正在导出";
            this.exporting = true;
            axios({
                method: "post",
                url: requestContext + "api/materials/export",
                responseType: "blob"
            }).then(function (response) {
                main.download(response);
                main.exportResult();
            }).catch(function (error) {
                popoverSpace.append("服务器访问失败", false);
                main.exportResult();
            });
        },
        exportResult: function () {
            this.exportAction = "导出";
            this.exporting = false;
        },
        download: function (response) {
            if (!response) {
                return;
            }
            let url = window.URL.createObjectURL(new Blob([response.data],
                {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"}));
            let link = document.createElement("a");
            link.style.display = "none";
            link.href = url;

            document.body.appendChild(link);
            link.click();
        }
    },
    mounted: function () {
        axios.get(requestContext + "api/materials?pageIndex=" + 1 + "&pageSize=" + 15 + "&status=" + 0)
            .then(function (response) {
                let statusCode = response.data.statusCode;
                if (200 === statusCode) {
                    let data = response.data.data;
                    main.pageContext.index = data.index;
                    main.pageContext.pageTotal = data.pageTotal;
                    main.pageContext.dataTotal = data.dataTotal;
                    main.materialList = data.data;
                } else {
                    popoverSpace.append("数据获取失败", false);
                }
                mask.loadStop();
            }).catch(function (error) {
            popoverSpace.append("服务器访问失败", false);
            console.log("FUCK" + error);
            mask.loadStop();
        });
    }
});

const modal = new Vue({
    el: "#modal",
    data: {
        show: false,
        picked: false,
        action: "导入",
        importing: false,
    },
    methods: {
        importModal: function () {
            this.show = true;
        },
        filePicker: function () {
            this.picked = true;
        },
        importFile: function () {
            if (this.picked) {
                this.importing = true;
                this.action = "正在导入";
                let file = document.getElementById("file").files[0];
                let param = new FormData();  //创建form对象
                param.append("file", file, file.name);  //通过append向form对象添加数据
                let config = {
                    headers: {"Content-Type": "multipart/form-data"}
                };  //添加请求头
                axios.post(requestContext + "api/materials/import", param, config)
                    .then(function (response) {
                        if (200 === response.data.statusCode) {
                            document.getElementById("file").value = "";
                            popoverSpace.append("导入成功，请刷新界面", true);
                            modal.picked = false;
                            modal.importResult();
                        } else {
                            popoverSpace.append("导入失败", false);
                            modal.importResult();
                        }
                    }).catch(function (error) {
                    popoverSpace.append("服务器访问失败", false);
                    modal.importResult();
                })
            }
        },
        importResult: function () {
            modal.action = "导入";
            modal.importing = false;
        }
    }
});