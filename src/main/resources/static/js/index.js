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
        exportAction: "导出最新",
        exportAllAction: "导出全部",
        isExportDisabled: false,
        isExportAllDisabled: false,
        isQueryDisabled: false,
        isSaveDisabled: false,
        exportStatus: 3,
        perfectStatus: 0,
        currentPage: "",
        materialCode: "",
        structureNo: "",
        resourceMark: "",
        user: {
            roles: {
                ROLE_TECHNOLOGY_EMPLOYEE: false,
                ROLE_QUALITY_EMPLOYEE: false,
                ROLE_PURCHASE_EMPLOYEE: false,
                ROLE_ASSEMBLY_EMPLOYEE: false,
                ROLE_PRODUCE_EMPLOYEE: false
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
        exportDataFileModalVisible: function () {
            dataFileExportModal.visible();
        },
        importProduceDataFileModalVisible: function () {
            produceDataFileImportModal.visible();
        },
        importAssemblyDataFileModalVisible: function () {
            assemblyDataFileImportModal.visible();
        },
        queryMaterialList: function (index, size, exportStatus, perfectStatus) {
            axios.get(requestContext + "api/materials?pageIndex=" + index + "&pageSize=" + size
                + "&exportStatus=" + exportStatus + "&perfectStatus=" + perfectStatus)
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
                }).catch(function (error) {
                console.log(error);
                popoverSpace.append("服务器访问失败", false);
                loadModal.loadCallback();
            });
        },
        queryAllByPreviousPage: function () {
            if (this.pageContext.index > 1) {
                this.queryMaterialList(this.pageContext.index - 1, this.pageContext.size, this.exportStatus, this.perfectStatus);
            }
        },
        queryAllByNextPage: function () {
            if (this.pageContext.index < this.pageContext.pageTotal) {
                this.queryMaterialList(this.pageContext.index + 1, this.pageContext.size, this.exportStatus, this.perfectStatus);
            }
        },
        showFileImportModal: function () {
            fileImportModal.visible();
        },
        showPBOMPerfectModal: function () {
            perfectPBOMModal.visible();
        },
        showPBOMImportModal: function () {
            importPBOMModal.visible();
        },
        showDataFileImportModal: function () {
            dataFileImportModal.visible();
        },
        queryAllByExportStatus: function () {
            this.perfectStatus = 2;
            this.queryMaterialList(1, this.pageContext.size, this.exportStatus, this.perfectStatus);
        },
        queryAllByPerfectStatus: function () {
            this.exportStatus = 3;
            this.queryMaterialList(1, this.pageContext.size, this.exportStatus, this.perfectStatus);
        },
        exportFile: function () {
            this.exportAction = "正在导出";
            this.isExportDisabled = true;
            axios({
                method: "post",
                url: requestContext + "api/materials/export",
                responseType: "blob"
            }).then(function (response) {
                download(response, "基础数据.xlsx");
                container.exportCallback();
            }).catch(function () {
                popoverSpace.append("服务器访问失败", false);
                container.exportCallback();
            });
        },
        exportProduceDataFile: function () {
            this.exportAction = "正在导出";
            this.isExportDisabled = true;
            let name = "基础数据-生产部-未完善.xlsx";
            axios({
                method: "post",
                params: {
                    deptMark: 5,
                    perfectStatus: 0
                },
                url: requestContext + "api/materials/export/dept",
                responseType: "blob"
            }).then(function (response) {
                download(response, name);
                container.exportCallback();
            }).catch(function () {
                popoverSpace.append("服务器访问失败", false);
                container.exportCallback();
            });
        },
        exportAssemblyDataFile: function () {
            this.exportAction = "正在导出";
            this.isExportDisabled = true;
            let name = "基础数据-集配中心-未完善.xlsx";
            axios({
                method: "post",
                params: {
                    deptMark: 4,
                    perfectStatus: 0
                },
                url: requestContext + "api/materials/export/dept",
                responseType: "blob"
            }).then(function (response) {
                download(response, name);
                container.exportCallback();
            }).catch(function () {
                popoverSpace.append("服务器访问失败", false);
                container.exportCallback();
            });
        },
        exportCallback: function () {
            this.exportAction = "导出最新";
            this.isExportDisabled = false;
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
            this.queryMaterialList(this.currentPage, this.pageContext.size, this.exportStatus, this.perfectStatus);
        },
        queryAllByMaterialCode: function () {
            if (this.materialCode.length < 1) {
                popoverSpace.append("模糊查询的物料号至少输入4位", false);
                return;
            }
            this.isQueryDisabled = true;
            this.exportStatus = 3;
            if (!this.user.roles.ROLE_TECHNOLOGY_EMPLOYEE) {
                this.perfectStatus = 2;
            }
            axios.get(requestContext + "api/materials/query?materialCode=" + this.materialCode
                + "&perfectStatus=" + this.perfectStatus)
                .then(function (response) {
                    let statusCode = response.data.statusCode;
                    if (200 === statusCode) {
                        let data = response.data.data;
                        let pageContext = {index: 1, pageTotal: 1, dataTotal: data.length};
                        container.setPageContext(pageContext);
                        container.setMaterialList(data);
                        if (0 === data.length) {
                            popoverSpace.append("未查询到数据", true);
                        }
                    } else {
                        popoverSpace.append("数据获取失败", false);
                    }
                    container.queryCallback();
                })
                .catch(function () {
                    popoverSpace.append("服务器访问失败", false);
                    container.queryCallback();
                });
        },
        queryAllByStructureNo: function () {
            this.isQueryDisabled = true;
            this.exportStatus = 3;
            if (!this.user.roles.ROLE_TECHNOLOGY_EMPLOYEE) {
                this.perfectStatus = 2;
            }
            axios.get(requestContext + "api/materials/query2?structureNo=" + this.structureNo
                + "&perfectStatus=" + this.perfectStatus)
                .then(function (response) {
                    let statusCode = response.data.statusCode;
                    if (200 === statusCode) {
                        let data = response.data.data;
                        let pageContext = {index: 1, pageTotal: 1, dataTotal: data.length};
                        container.setPageContext(pageContext);
                        container.setMaterialList(data);
                        if (0 === data.length) {
                            popoverSpace.append("未查询到数据", true);
                        }
                    } else {
                        popoverSpace.append("数据获取失败", false);
                    }
                    container.queryCallback();
                })
                .catch(function () {
                    popoverSpace.append("服务器访问失败", false);
                    container.queryCallback();
                });
        },
        queryAllByResourceMark: function () {
            this.isQueryDisabled = true;
            this.exportStatus = 3;
            if (!this.user.roles.ROLE_TECHNOLOGY_EMPLOYEE) {
                this.perfectStatus = 2;
            }
            axios.get(requestContext + "api/materials/query3?resourceMark=" + this.resourceMark
                + "&perfectStatus=" + this.perfectStatus)
                .then(function (response) {
                    let statusCode = response.data.statusCode;
                    if (200 === statusCode) {
                        let data = response.data.data;
                        let pageContext = {index: 1, pageTotal: 1, dataTotal: data.length};
                        container.setPageContext(pageContext);
                        container.setMaterialList(data);
                        if (0 === data.length) {
                            popoverSpace.append("未查询到数据", true);
                        }
                    } else {
                        popoverSpace.append("数据获取失败", false);
                    }
                    container.queryCallback();
                })
                .catch(function () {
                    popoverSpace.append("服务器访问失败", false);
                    container.queryCallback();
                });
        },
        queryCallback: function () {
            this.isQueryDisabled = false;
        },
        selectRespDepts: function (index, isPersist) {
            for (let i = 0; i < this.responsibility.length; i++) {
                if (this.materialList[index].respCompany === this.responsibility[i].company) {
                    this.materialList[index].respDepts = this.responsibility[i].department;
                    break;
                }
            }
            if (!isPersist) {
                this.materialList[index].respDept = null;
            }
        },
        selectKeyPartMark: function (index) {
            if (null === this.materialList[index].keyPartMark || "null" === this.materialList[index].keyPartMark) {
                this.materialList[index].keyPartMark = null;
                this.materialList[index].keyPartSort = null;
            }
        },
        selectKeyPartSort: function (index) {
            this.materialList[index].keyPartMark = "Y";
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
        saveTechnologyAttr: function (material, index) {
            if (null === material.generalSort || "" === material.generalSort) {
                popoverSpace.append("请选择普通分类", false);
                return;
            }
            if (null === material.inventoryUnit || "" === material.inventoryUnit) {
                popoverSpace.append("请选择库存单位", false);
                return;
            }
            if (null === material.sourceMark || "" === material.sourceMark) {
                popoverSpace.append("请选择采购自制件标记", false);
                return;
            }
            if (null === material.respCompany || "" === material.respCompany) {
                popoverSpace.append("请选择责任公司", false);
                return;
            }
            if (null === material.respDept || "" === material.respDept) {
                popoverSpace.append("请选择责任部门", false);
                return;
            }
            if (null === material.virtualPartMark || "" === material.virtualPartMark) {
                popoverSpace.append("请选择虚拟件标记", false);
                return;
            }
            if (null === material.qualifiedMark || "" === material.qualifiedMark) {
                popoverSpace.append("请选择合批标记", false);
                return;
            }
            this.isSaveDisabled = true;
            axios.put(requestContext + "api/materials/technology", material)
                .then(function (response) {
                    container.saveSuccessCallback(response.data.statusCode);
                }).catch(function () {
                container.saveErrorCallback();
            });
        },
        saveQualityAttr: function (material, index) {
            if (null === material.inspectMark || "" === material.inspectMark) {
                popoverSpace.append("请选择质检标记", false);
                return;
            }
            if (null === material.batchMark || "" === material.batchMark) {
                popoverSpace.append("请选择批次标记", false);
                return;
            }
            this.isSaveDisabled = true;
            axios.put(requestContext + "api/materials/quality", material)
                .then(function (response) {
                    container.saveSuccessCallback(response.data.statusCode);
                }).catch(function () {
                container.saveErrorCallback();
            });
        },
        savePurchaseAttr: function (material, index) {
            if (null === material.purchaseSort || "" === material.purchaseSort) {
                popoverSpace.append("请选择采购分类", false);
                return;
            }
            if (null === material.purchaseMark || "" === material.purchaseMark) {
                popoverSpace.append("请选择可采购标记", false);
                return;
            }
            if (null === material.fixedAdvTime || "" === material.fixedAdvTime) {
                popoverSpace.append("请输入固定提前期", false);
                return;
            }
            this.isSaveDisabled = true;
            axios.put(requestContext + "api/materials/purchase", material)
                .then(function (response) {
                    container.saveSuccessCallback(response.data.statusCode);
                }).catch(function () {
                container.saveErrorCallback();
            });
        },
        saveAssemblyAttr: function (material, index) {
            if (null === material.defRepository || "" === material.defRepository) {
                popoverSpace.append("请选择默认仓库", false);
                return;
            }
            this.isSaveDisabled = true;
            axios.put(requestContext + "api/materials/assembly", material)
                .then(function (response) {
                    container.saveSuccessCallback(response.data.statusCode);
                }).catch(function () {
                container.saveErrorCallback();
            });
        },
        saveProduceAttr: function (material, index) {
            if (null === material.outSource || "" === material.outSource) {
                popoverSpace.append("请填写外协标记", false);
                return;
            }
            if (null === material.planner || "" === material.planner) {
                popoverSpace.append("请填写计划员", false);
                return;
            }
            if (null === material.fixedAdvTime || "" === material.fixedAdvTime) {
                popoverSpace.append("请填写固定提前期", false);
                return;
            }
            this.isSaveDisabled = true;
            axios.put(requestContext + "api/materials/produce", material)
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
        exportAllFile: function () {
            this.exportAllAction = "正在导出";
            this.isExportAllDisabled = true;
            axios({
                method: "post",
                url: requestContext + "api/materials/export/all",
                responseType: "blob"
            }).then(function (response) {
                let date = new Date();
                download(response, "基础数据" + date + ".xlsx");
                container.exportAllCallback();
            }).catch(function (error) {
                console.log(error);
                popoverSpace.append("服务器访问失败", false);
                container.exportAllCallback();
            });
        },
        exportAllCallback: function () {
            this.exportAllAction = "导出全部";
            this.isExportAllDisabled = false;
        }
    },
    mounted: function () {
        this.user = JSON.parse(localStorage.user);
        this.queryMaterialList(this.pageContext.index, this.pageContext.size, this.exportStatus, this.perfectStatus);
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
            axios.post(requestContext + "api/materials/import", param, config)
                .then(function (response) {
                    let statusCode = response.data.statusCode;
                    if (200 === statusCode) {
                        let content = "";
                        for (let index = 0; index < response.data.data.length; index++) {
                            content += "第 " + response.data.data[index].order + " 行 ";
                            content += response.data.data[index].message;
                            content += "\r\n";
                        }
                        createAndDownload(name + "导入备注.txt", content);
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

const perfectPBOMModal = new Vue({
    el: "#perfectPBOMModal",
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
            if ("" === document.getElementById("toPerfectPBOM").value) {
                popoverSpace.append("请选择BOM文件", true);
                return;
            }
            this.isDisabled = true;
            this.action = "正在完善";
            let file = document.getElementById("toPerfectPBOM").files[0];
            let param = new FormData();  //创建form对象
            param.append("file", file, file.name);  //通过append向form对象添加数据

            let fileName = document.getElementById("toPerfectPBOM").files[0].name;

            axios({
                method: "post",
                url: requestContext + "api/materials/perfectpbom",
                headers: {'Content-Type': "multipart/form-data"},  //定义响应头
                responseType: 'blob',
                data: param
            }).then(function (response) {
                download(response, fileName);
                perfectPBOMModal.importCallback();
            }).catch(function () {
                popoverSpace.append("服务器访问失败", false);
                perfectPBOMModal.importCallback();
            });
        },
        importCallback: function () {
            this.action = "导入";
            this.isDisabled = false;
        }
    }
});

const importPBOMModal = new Vue({
    el: "#importPBOMModal",
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
            if ("" === document.getElementById("toImportPBOM").value) {
                popoverSpace.append("请选择BOM文件", true);
                return;
            }
            this.isDisabled = true;
            this.action = "正在导入";
            let file = document.getElementById("toImportPBOM").files[0];
            let param = new FormData();  //创建form对象
            param.append("file", file, file.name);  //通过append向form对象添加数据
            let config = {
                headers: {"Content-Type": "multipart/form-data"}
            };  //添加请求头
            axios.post(requestContext + "api/materials/importpbom", param, config)
                .then(function (response) {
                    let statusCode = response.data.statusCode;
                    if (200 === statusCode) {
                        let content = "";
                        for (let index = 0; index < response.data.data.length; index++) {
                            content += "第 " + response.data.data[index].order + " 行 ";
                            content += response.data.data[index].message;
                            content += "\r\n";
                        }
                        createAndDownload(name + "导入备注.txt", content);
                        window.location.reload();
                    } else {
                        let message = getMessage(statusCode);
                        popoverSpace.append(message, false);
                    }
                    fileImportModal.importCallback();
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

const dataFileImportModal = new Vue({
    el: "#dataFileImportModal",
    data: {
        sheetName: "data",
        deptMark: "3",
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
            if ("" === document.getElementById("dataFile").value) {
                popoverSpace.append("请选择基础数据文件", true);
                return;
            }
            this.isDisabled = true;
            this.action = "正在导入";
            let file = document.getElementById("dataFile").files[0];
            let param = new FormData();  //创建form对象
            param.append("file", file, file.name);  //通过append向form对象添加数据
            param.append("sheetName", this.sheetName);
            param.append("deptMark", this.deptMark);
            let config = {
                headers: {"Content-Type": "multipart/form-data"}
            };  //添加请求头
            axios.post(requestContext + "api/materials/perfect", param, config)
                .then(function (response) {
                    let statusCode = response.data.statusCode;
                    if (200 === statusCode) {
                        window.location.reload();
                    } else {
                        let message = getMessage(statusCode);
                        popoverSpace.append(message, false);
                        fileImportModal.importCallback();
                    }
                }).catch(function () {
                popoverSpace.append("服务器访问失败", false);
                dataFileImportModal.importCallback();
            })
        },
        importCallback: function () {
            this.action = "导入";
            this.isDisabled = false;
        }
    }
});

const produceDataFileImportModal = new Vue({
    el: "#produceDataFileImportModal",
    data: {
        sheetName: "data",
        deptMark: "5",
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
            if ("" === document.getElementById("produceFile").value) {
                popoverSpace.append("请选择基础数据文件", true);
                return;
            }
            this.isDisabled = true;
            this.action = "正在导入";
            let file = document.getElementById("produceFile").files[0];
            let param = new FormData();  //创建form对象
            param.append("file", file, file.name);  //通过append向form对象添加数据
            param.append("sheetName", this.sheetName);
            param.append("deptMark", this.deptMark);
            let config = {
                headers: {"Content-Type": "multipart/form-data"}
            };  //添加请求头
            axios.post(requestContext + "api/materials/perfect", param, config)
                .then(function (response) {
                    let statusCode = response.data.statusCode;
                    if (200 === statusCode) {
                        window.location.reload();
                    } else {
                        let message = getMessage(statusCode);
                        popoverSpace.append(message, false);
                        fileImportModal.importCallback();
                    }
                }).catch(function () {
                popoverSpace.append("服务器访问失败", false);
                dataFileImportModal.importCallback();
            })
        },
        importCallback: function () {
            this.action = "导入";
            this.isDisabled = false;
        }
    }
});

const assemblyDataFileImportModal = new Vue({
    el: "#assemblyDataFileImportModal",
    data: {
        sheetName: "data",
        deptMark: "4",
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
            if ("" === document.getElementById("assemblyFile").value) {
                popoverSpace.append("请选择基础数据文件", true);
                return;
            }
            this.isDisabled = true;
            this.action = "正在导入";
            let file = document.getElementById("assemblyFile").files[0];
            let param = new FormData();  //创建form对象
            param.append("file", file, file.name);  //通过append向form对象添加数据
            param.append("sheetName", this.sheetName);
            param.append("deptMark", this.deptMark);
            let config = {
                headers: {"Content-Type": "multipart/form-data"}
            };  //添加请求头
            axios.post(requestContext + "api/materials/perfect", param, config)
                .then(function (response) {
                    let statusCode = response.data.statusCode;
                    if (200 === statusCode) {
                        window.location.reload();
                    } else {
                        let message = getMessage(statusCode);
                        popoverSpace.append(message, false);
                        fileImportModal.importCallback();
                    }
                }).catch(function () {
                popoverSpace.append("服务器访问失败", false);
                dataFileImportModal.importCallback();
            })
        },
        importCallback: function () {
            this.action = "导入";
            this.isDisabled = false;
        }
    }
});

const dataFileExportModal = new Vue({
    el: "#dataFileExportModal",
    data: {
        deptMark: "3",
        isVisible: false,
        isDisabled: false,
        action: "导出",
        perfectStatus: "0"
    },
    methods: {
        visible: function () {
            this.isVisible = true;
        },
        invisible: function () {
            this.isVisible = false;
        },
        exportDataFile: function () {
            this.action = "正在导出";
            this.isDisabled = true;
            let name = "基础数据-采购部-";
            if ("0" == this.perfectStatus) {
                name += "未完善";
            } else if ("1" == this.perfectStatus) {
                name += "已完善";
            }
            name += ".xlsx";
            let perfectStatus = this.perfectStatus;
            axios({
                method: "post",
                params: {
                    deptMark: 3,
                    perfectStatus: perfectStatus
                },
                url: requestContext + "api/materials/export/dept",
                responseType: "blob"
            }).then(function (response) {
                download(response, name);
                dataFileExportModal.exportCallback();
            }).catch(function () {
                popoverSpace.append("服务器访问失败", false);
                dataFileExportModal.exportCallback();
            });
        },
        exportCallback: function () {
            this.action = "导出";
            this.isDisabled = false;
        },
    }
});