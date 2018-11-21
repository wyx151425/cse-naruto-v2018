const main = new Vue({
    el: "#main",
    data: {
        user: {
            permissions: {
                MATERIAL_EDIT_TECH_PART: false,
                MATERIAL_EDIT_QA_ENV_PART: false,
                MATERIAL_EDIT_PURCHASE_PART: false,
                MATERIAL_EDIT_ASSEMBLY_PART: false,
                MATERIAL_EDIT_PRO_OPE_PART: false
            }
        },
        queryStr: "",
        loading: true,
        status: 1,
        prefect: 0,
        import: false,
        exportAction: "导出",
        exporting: false,
        pageContext: {
            index: 1,
            size: 100,
            pageTotal: 0,
            dataTotal: 0,
        },
        materialList: [],
        targetPage: "",
        isDisabled: false,
        action: "查询"
    },
    methods: {
        materialListQuery: function (index, size, status, prefect) {
            axios.get(requestContext + "api/materials?pageIndex=" + index + "&pageSize=" + size
                + "&status=" + status + "&prefect=" +prefect)
                .then(function (response) {
                    let statusCode = response.data.statusCode;
                    if (200 === statusCode) {
                        let data = response.data.data;
                        main.pageContext.index = data.index;
                        main.pageContext.pageTotal = data.pageTotal;
                        main.pageContext.dataTotal = data.dataTotal;
                        main.materialList = data.data;
                    } else {
                        let message = getMessage(statusCode);
                        popoverSpace.append(message, false);
                    }
                }).catch(function () {
                popoverSpace.append("服务器访问失败", false);
            });
        },
        previousPage: function () {
            if (this.pageContext.index > 1) {
                this.materialListQuery(this.pageContext.index - 1, this.pageContext.size, this.status, this.prefect);
            }
        },
        nextPage: function () {
            if (this.pageContext.index < this.pageContext.pageTotal) {
                this.materialListQuery(this.pageContext.index + 1, this.pageContext.size, this.status, this.prefect);
            }
        },
        importModal: function () {
            modal.importModal();
        },
        statusQuery: function () {
            this.materialListQuery(1, 100, this.status, 2);
        },
        prefectQuery: function () {
            this.materialListQuery(1, 100, 0, this.prefect);
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
        },
        pageSearch: function () {
            if ("" === this.targetPage || "0" === this.targetPage) {
                popoverSpace.append("请输入查找页数", false);
                return;
            }
            if (this.targetPage > this.pageContext.pageTotal) {
                popoverSpace.append("超过最大数据页数", false);
                return;
            }
            mask.loadStart();
            axios.get(requestContext + "api/materials?pageIndex=" + this.targetPage
                + "&pageSize=" + this.pageContext.size + "&status=" + this.status + "&prefect=" + this.prefect)
                .then(function (response) {
                    let statusCode = response.data.statusCode;
                    if (200 === statusCode) {
                        let data = response.data.data;
                        main.pageContext.index = data.index;
                        main.pageContext.pageTotal = data.pageTotal;
                        main.pageContext.dataTotal = data.dataTotal;
                        main.materialList = data.data;
                    } else {
                        let message = getMessage(statusCode);
                    }
                    mask.loadStop();
                }).catch(function (error) {
                popoverSpace.append("服务器访问失败", false);
                mask.loadStop();
            });
        },
        queryMaterialList: function () {
            this.isDisabled = true;
            this.action = "查询中";
            axios.get(requestContext + "api/materials/query?queryStr=" + this.queryStr)
                .then(function (response) {
                    let statusCode = response.data.statusCode;
                    if (200 === statusCode) {
                        main.setMaterialListData(response.data.data);
                    } else {
                        popoverSpace.append("数据获取失败", false);
                    }
                    main.queryCallback();
                })
                .catch(function () {
                    popoverSpace.append("服务器访问失败", false);
                    main.queryCallback();
                });
        },
        queryCallback: function () {
            this.action = "查询";
            this.isDisabled = false;
        },
        setMaterialListData: function (data) {
            this.pageContext.index = 1;
            this.pageContext.pageTotal = 1;
            this.pageContext.dataTotal = data.length;
            this.materialList = data;
        },
    },
    mounted: function () {
        this.user = JSON.parse(localStorage.user);
        if (this.user.roles.ROLE_TECH_EMPLOYEE) {
            this.prefect = 2;
        }
        if (!this.user.roles.ROLE_TECH_EMPLOYEE) {
            this.prefect = 0;
        }
        axios.get(requestContext + "api/materials?pageIndex=" + this.pageContext.index
            + "&pageSize=" + this.pageContext.size + "&status=" + this.status + "&prefect=" + this.prefect)
            .then(function (response) {
                let statusCode = response.data.statusCode;
                if (200 === statusCode) {
                    let data = response.data.data;
                    main.pageContext.index = data.index;
                    main.pageContext.pageTotal = data.pageTotal;
                    main.pageContext.dataTotal = data.dataTotal;
                    main.materialList = data.data;
                } else {
                    let message = getMessage(statusCode);
                    popoverSpace.append(message, false);
                }
                mask.loadStop();
            }).catch(function (error) {
            popoverSpace.append("服务器访问失败", false);
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
                        let statusCode = response.data.statusCode;
                        if (200 === statusCode) {
                            document.getElementById("file").value = "";
                            popoverSpace.append("导入成功，请刷新界面", true);
                            modal.picked = false;
                            modal.importResult();
                        } else {
                            let message = getMessage(statusCode);
                            popoverSpace.append(message, false);
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