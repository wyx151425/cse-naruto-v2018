window.onload = function () {
    let tableSpace = document.querySelector("#table-space");

    function handleScroll() {
        let scrollTop = this.scrollTop;
        this.querySelector("thead").style.transform = "translate(0px, " + scrollTop + "px) translateZ(999px)";

        let scrollLeft = this.scrollLeft;
        let ths = this.querySelectorAll("th[class=table-fixed]");
        let tds = this.querySelectorAll("td[class=table-fixed]");
        for (let index = 0; index < ths.length; index++) {
            ths[index].style.transform = "translate(" + scrollLeft + "px, 0px) translateZ(999px)";
        }
        for (let index = 0; index < tds.length; index++) {
            tds[index].style.transform = "translate(" + scrollLeft + "px, 0px) translateZ(900px)";
        }
    }

    tableSpace.addEventListener("scroll", handleScroll);
};

const container = new Vue({
    el: "#container",
    data: {
        isSaveDisabled: false,
        isConfirmDisabled: false,
        currentPage: "",
        user: {
            roles: {
                ROLE_TECHNOLOGY_EMPLOYEE: false,
                ROLE_QUALITY_EMPLOYEE: false,
                ROLE_PURCHASE_EMPLOYEE: false,
                ROLE_ASSEMBLY_EMPLOYEE: false,
                ROLE_PRODUCE_EMPLOYEE: false,
                ROLE_GUARANTEE_EMPLOYEE: false
            }
        },
        pageContext: {
            index: 1,
            size: 20,
            pageTotal: 0,
            dataTotal: 0
        },
        materialList: [],
        responsibility: [
            {
                company: "01",
                department: [
                    {
                        name: "办公室",
                        code: "6001"
                    },
                    {
                        name: "战略规划发展部",
                        code: "6002"
                    },
                    {
                        name: "生产运行部",
                        code: "6003"
                    },
                    {
                        name: "战略采购部",
                        code: "6004"
                    },
                    {
                        name: "技术中心",
                        code: "6005"
                    },
                    {
                        name: "质量安全环保部",
                        code: "6006"
                    },
                    {
                        name: "人力资源部",
                        code: "6007"
                    },
                    {
                        name: "财务部",
                        code: "6008"
                    },
                    {
                        name: "审计部",
                        code: "6009"
                    },
                    {
                        name: "纪检监察部",
                        code: "6010"
                    },
                    {
                        name: "经销公司",
                        code: "6020"
                    },
                    {
                        name: "柴油机销售部",
                        code: "6021"
                    },
                    {
                        name: "国际业务部",
                        code: "6022"
                    },
                    {
                        name: "市场部",
                        code: "6023"
                    },
                    {
                        name: "服务保障中心",
                        code: "6024"
                    }
                ]
            },
            {
                company: "02",
                department: [
                    {
                        name: "综合办公室",
                        code: "6100"
                    },
                    {
                        name: "销售部",
                        code: "6101"
                    },
                    {
                        name: "生产部",
                        code: "6102"
                    },
                    {
                        name: "采购部",
                        code: "6103"
                    },
                    {
                        name: "制造研发部",
                        code: "6104"
                    },
                    {
                        name: "安全环保部",
                        code: "6105"
                    },
                    {
                        name: "质量合规部",
                        code: "6106"
                    },
                    {
                        name: "财务室",
                        code: "6107"
                    },
                    {
                        name: "审计室",
                        code: "6108"
                    },
                    {
                        name: "纪检监察部",
                        code: "6109"
                    },
                    {
                        name: "铸造厂",
                        code: "6110"
                    },
                    {
                        name: "钢构厂",
                        code: "6111"
                    },
                    {
                        name: "机加一厂",
                        code: "6112"
                    },
                    {
                        name: "机加二厂",
                        code: "6113"
                    },
                    {
                        name: "总装厂",
                        code: "6114"
                    },
                    {
                        name: "集配中心",
                        code: "6115"
                    },
                    {
                        name: "物业服务部",
                        code: "6116"
                    }
                ]
            },
            {
                company: "03",
                department: [
                    {
                        name: "生产部",
                        code: "6201"
                    },
                    {
                        name: "采购部",
                        code: "6202"
                    },
                    {
                        name: "安全环保部",
                        code: "6203"
                    },
                    {
                        name: "质量合规部",
                        code: "6204"
                    },
                    {
                        name: "钢构厂",
                        code: "6205"
                    },
                    {
                        name: "机加厂",
                        code: "6206"
                    },
                    {
                        name: "总装厂",
                        code: "6207"
                    },
                    {
                        name: "集配中心",
                        code: "6208"
                    }
                ]
            }
        ]
    },
    methods: {
        setMaterialList: function (materialList) {
            this.materialList = materialList;
            for (let index = 0; index < this.materialList.length; index++) {
                this.selectRespDepts(index, true);
            }
        },
        setPageContext: function (pageContext) {
            this.pageContext.index = pageContext.index;
            this.pageContext.pageTotal = pageContext.pageTotal;
            this.pageContext.dataTotal = pageContext.dataTotal;
        },
        showFileImportModal: function () {
            fileImportModal.visible();
        },
        showConfirmPageModal: function () {
            confirmPageModal.visible();
        },
        queryAllByPreviousPage: function () {
            if (this.pageContext.index > 1) {
                this.queryGuaranteeMaterialList(this.pageContext.index - 1, this.pageContext.size);
            }
        },
        queryAllByNextPage: function () {
            if (this.pageContext.index < this.pageContext.pageTotal) {
                this.queryGuaranteeMaterialList(this.pageContext.index + 1, this.pageContext.size);
            }
        },
        queryAllByPage: function () {
            if ("" === this.currentPage || "0" === this.currentPage) {
                popoverSpace.append("请输入查找页数", false);
                return;
            }
            if (this.currentPage > this.pageContext.pageTotal) {
                popoverSpace.append("超过最大数据页数", false);
                return;
            }
            this.queryGuaranteeMaterialList(this.currentPage, this.pageContext.size);
        },
        queryCallback: function () {
            this.isQueryDisabled = false;
        },
        selectRespDepts: function (index) {
            for (let i = 0; i < this.responsibility.length; i++) {
                if (this.materialList[index].respCompany === this.responsibility[i].company) {
                    this.materialList[index].respDepts = this.responsibility[i].department;
                    break;
                }
            }
        },
        toggleGroupPurMark: function (index) {
            if ("Y" === this.materialList[index].groupPurMark) {
                this.materialList[index].ownPurMark = "N";
            } else if ("N" === this.materialList[index].groupPurMark) {
                this.materialList[index].ownPurMark = "Y";
            }
        },
        toggleOwnPurMark: function (index) {
            if ("Y" === this.materialList[index].ownPurMark) {
                this.materialList[index].groupPurMark = "N";
            } else if ("N" === this.materialList[index].ownPurMark) {
                this.materialList[index].groupPurMark = "Y";
            }
        },
        saveMaterial: function (material) {
            if (null === material.fixedAdvTime || "" === material.fixedAdvTime) {
                popoverSpace.append("请输入固定提前期", false);
                return;
            }
            this.isSaveDisabled = true;
            axios.put(requestContext + "api/materials", material)
                .then(function (response) {
                    container.saveSuccessCallback(response.data.statusCode);
                }).catch(function () {
                container.saveErrorCallback();
            });
        },
        saveSuccessCallback: function (statusCode) {
            if (200 === statusCode) {
                popoverSpace.append("保存成功", true);
            } else {
                let message = getMessage(statusCode);
                popoverSpace.append(message, false);
            }
            this.isSaveDisabled = false;
        },
        saveErrorCallback: function () {
            popoverSpace.append("服务器访问失败", false);
            this.isSaveDisabled = false;
        },
        queryGuaranteeMaterialList: function (index, size) {
            axios.get(requestContext + "api/materials/guarantee?pageIndex=" + index + "&pageSize=" + size)
                .then(function (response) {
                    let statusCode = response.data.statusCode;
                    if (200 === statusCode) {
                        let pageContext = response.data.data;
                        container.setPageContext(pageContext);
                        container.setMaterialList(pageContext.data);
                    } else {
                        let message = getMessage(statusCode);
                        popoverSpace.append(message, false);
                    }
                    loadModal.loadCallback();
                }).catch(function () {
                popoverSpace.append("服务器访问失败", false);
                loadModal.loadCallback();
            });
        },
        confirmMaterial: function (material) {
            this.isConfirmDisabled = true;
            axios.put(requestContext + "api/materials/confirmOne", material)
                .then(function (response) {
                    let statusCode = response.data.statusCode;
                    if (200 === statusCode) {
                        popoverSpace.append("确认成功", true);
                    } else {
                        let message = getMessage(statusCode);
                        popoverSpace.append(message, false);
                    }
                }).catch(function () {
                popoverSpace.append("服务器访问失败", false);
                main.confirmCallback();
            });
        },
        confirmAll: function () {
            axios.put(requestContext + "api/materials/confirmAll", this.materialList)
                .then(function (response) {
                    let statusCode = response.data.statusCode;
                    if (200 === statusCode) {
                        popoverSpace.append("确认成功", true);
                    } else {
                        let message = getMessage(statusCode);
                        popoverSpace.append(message, false);
                    }
                    confirmPageModal.confirmCallback();
                }).catch(function () {
                popoverSpace.append("服务器访问失败", false);
                confirmPageModal.confirmCallback();
            });
        },
        confirmCallback: function () {
            this.isConfirmDisabled = false;
        }
    },
    mounted: function () {
        this.user = JSON.parse(localStorage.user);
        this.queryGuaranteeMaterialList(this.pageContext.index, this.pageContext.size, this.exportStatus, this.perfectStatus);
    }
});

const fileImportModal = new Vue({
    el: "#fileImportModal",
    data: {
        isVisible: false,
        isDisabled: false,
        action: "导入"
    },
    methods: {
        visible: function () {
            this.isVisible = true;
        },
        invisible: function () {
            this.isVisible = false;
        },
        importFile: function () {
            if ("" === document.getElementById("file").value) {
                popoverSpace.append("请选择BOM文件", true);
                return;
            }
            this.isDisabled = true;
            this.action = "正在导入";
            let file = document.getElementById("file").files[0];
            let param = new FormData();  //创建form对象
            param.append("file", file, file.name);  //通过append向form对象添加数据
            let config = {
                headers: {"Content-Type": "multipart/form-data"}
            };  //添加请求头
            axios.post(requestContext + "api/materials/guaranteeImport", param, config)
                .then(function (response) {
                    let statusCode = response.data.statusCode;
                    if (200 === statusCode) {
                        popoverSpace.append("导入完毕", true);
                        window.location.reload();
                    } else {
                        let message = getMessage(statusCode);
                        popoverSpace.append(message, false);
                        fileImportModal.importCallback();
                    }
                }).catch(function () {
                popoverSpace.append("服务器访问失败", false);
                fileImportModal.importCallback();
            })
        },
        importCallback: function () {
            this.action = "导入";
            this.isDisabled = false;
        }
    }
});

const confirmPageModal = new Vue({
    el: "#confirmPageModal",
    data: {
        isVisible: false,
        isConfirmDisabled: false
    },
    methods: {
        visible: function () {
            this.isVisible = true;
        },
        invisible: function () {
            this.isVisible = false;
        },
        confirmPage: function () {
            this.isConfirmDisabled = true;
            container.confirmAll();
        },
        confirmCallback: function () {
            this.isConfirmDisabled = false;
        }
    }
});