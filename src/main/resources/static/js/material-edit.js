const main = new Vue({
    el: "#main",
    data: {
        loading: true,
        user: {
            permissions: {
                MATERIAL_EDIT_TECH_PART: false,
                MATERIAL_EDIT_QA_ENV_PART: false,
                MATERIAL_EDIT_PURCHASE_PART: false,
                MATERIAL_EDIT_ASSEMBLY_PART: false,
                MATERIAL_EDIT_PRO_OPE_PART: false
            }
        },
        material: {
            companyNo: "",
            code: "",
            name: "",
            shortName: "",
            specification: "",
            model: "",
            description: "",
            drawingNo: "",
            generalSort: "请选择",
            inventoryUnit: "请选择",
            sourceMark: "请选择",
            respCompany: "请选择",
            respDept: "请选择",
            keyPartMark: "请选择",
            keyPartSort: "请选择",
            virtualPartMark: "请选择",
            qualifiedMark: "请选择",
            inspectMark: "请选择",
            batchMark: "请选择",
            purchaseSort: "请选择",
            purchaseMark: "请选择",
            groupPurMark: "请选择",
            ownPurMark: "请选择",
            defRepository: "请选择",
            outSource: "请选择",
            planner: "",
            fixedAdvTime: ""
        },
        responsibility: [
            {
                company: "中国船柴",
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
                company: "宜昌基地",
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
                company: "青岛基地",
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
            },
            {
                company: "大连基地",
                department: [
                    {
                        name: "综合办公室",
                        code: "6301"
                    },
                    {
                        name: "人力资源部",
                        code: "6302"
                    },
                    {
                        name: "纪检监察部",
                        code: "6303"
                    },
                    {
                        name: "销售部",
                        code: "6304"
                    },
                    {
                        name: "生产部",
                        code: "6305"
                    },
                    {
                        name: "采购部",
                        code: "6306"
                    },
                    {
                        name: "安全环保部",
                        code: "6307"
                    },
                    {
                        name: "质量合规部",
                        code: "6308"
                    },
                    {
                        name: "机动保障部",
                        code: "6309"
                    },
                    {
                        name: "钢构制造部",
                        code: "6310"
                    },
                    {
                        name: "机械加工部",
                        code: "6311"
                    },
                    {
                        name: "总装制造部",
                        code: "6312"
                    },
                    {
                        name: "集配中心",
                        code: "6313"
                    },
                    {
                        name: "船厂服务部",
                        code: "6314"
                    }
                ]
            }
        ],
        department: [],
        action: "保存",
        saving: false
    },
    methods: {
        respDeptSelect: function () {
            for (let index = 0; index < this.responsibility.length; index++) {
                if (this.material.respCompany === this.responsibility[index].company) {
                    this.department = this.responsibility[index].department;
                    break;
                }
            }
            this.material.respDept = "请选择";
        },
        keyPartMarkSelect: function () {
            if ("Y" === this.material.keyPartMark) {
                this.material.keyPartSort = "请选择";
            }
            if ("" === this.material.keyPartMark) {
                this.material.keyPartSort = "";
            }
        },
        saveStart: function () {
            this.saving = true;
            this.action = "正在保存";
        },
        saveStop: function () {
            this.action = "保存";
            this.saving = false;
        },
        technologyCenterSave: function () {
            if ("" === this.material.code) {
                return;
            }
            let material = {};
            material.code = this.material.code;
            if ("" === this.material.name) {
                popoverSpace.append("请输入物料名称", false);
                return;
            }
            material.name = this.material.name;
            if ("" === this.material.shortName) {
                popoverSpace.append("请输入物料简称", false);
                return;
            }
            material.shortName = this.material.shortName;
            if ("" === this.material.specification) {
                popoverSpace.append("请输入规格", false);
                return;
            }
            material.specification = this.material.specification;
            if ("" === this.material.model) {
                popoverSpace.append("请输入型号", false);
                return;
            }
            material.model = this.material.model;
            if ("" === this.material.drawingNo) {
                popoverSpace.append("请输入图号", false);
                return;
            }
            material.drawingNo = this.material.drawingNo;
            if ("请选择" === this.material.generalSort) {
                popoverSpace.append("请选择普通分类", false);
                return;
            }
            material.generalSort = this.material.generalSort;
            if ("请选择" === this.material.inventoryUnit) {
                popoverSpace.append("请选择库存单位", false);
                return;
            }
            material.inventoryUnit = this.material.inventoryUnit;
            if ("请选择" === this.material.sourceMark) {
                popoverSpace.append("请选择采购自制件标记", false);
                return;
            }
            material.sourceMark = this.material.sourceMark;
            if ("请选择" === this.material.respCompany) {
                popoverSpace.append("请选择责任公司", false);
                return;
            }
            material.respCompany = this.material.respCompany;
            if ("请选择" === this.material.respDept) {
                popoverSpace.append("请选择责任部门", false);
                return;
            }
            material.respDept = this.material.respDept;
            if ("请选择" === this.material.keyPartMark) {
                popoverSpace.append("请选择关键件标记", false);
                return;
            }
            material.keyPartMark = this.material.keyPartMark;
            if ("请选择" === this.material.keyPartSort) {
                popoverSpace.append("请选择关键件大类", false);
                return;
            }
            material.keyPartSort = this.material.keyPartSort;
            material.virtualPartMark = this.material.virtualPartMark;
            material.qualifiedMark = this.material.qualifiedMark;
            if ("" === this.material.description) {
                popoverSpace.append("请填写具体描述", false);
                return;
            }
            material.description = this.material.description;
            this.saveStart();
            axios.put(requestContext + "api/materials/technologyCenter", material)
                .then(function (response) {
                    let statusCode = response.data.statusCode;
                    if (200 === statusCode) {
                        popoverSpace.append("保存成功", true);
                        main.saveStop();
                    } else {
                        let message = getMessage(statusCode);
                        popoverSpace.append(message, false);
                        main.saveStop();
                    }
                }).catch(function () {
                popoverSpace.append("服务器访问失败", false);
                main.saveStop();
            });
        },
        qualifiedEnvironmentSave: function () {
            if ("" === this.material.code) {
                return;
            }
            let material = {};
            material.code = this.material.code;
            if ("请选择" === this.material.inspectMark) {
                popoverSpace.append("请选择质检标记", false);
                return;
            }
            material.inspectMark = this.material.inspectMark;
            if ("请选择" === this.material.batchMark) {
                popoverSpace.append("请选择批次标记", false);
                return;
            }
            material.batchMark = this.material.batchMark;
            this.saveStart();
            axios.put(requestContext + "api/materials/qualifiedEnvironment", material)
                .then(function (response) {
                    let statusCode = response.data.statusCode;
                    if (200 === statusCode) {
                        popoverSpace.append("保存成功", true);
                        main.saveStop();
                    } else {
                        let message = getMessage(statusCode);
                        popoverSpace.append(message, false);
                        main.saveStop();
                    }
                }).catch(function () {
                popoverSpace.append("服务器访问失败", false);
                main.saveStop();
            });
        },
        purchaseSave: function () {
            if ("" === this.material.code) {
                return;
            }
            let material = {};
            material.code = this.material.code;
            if ("请选择" === this.material.purchaseSort) {
                popoverSpace.append("请选择采购分类", false);
                return;
            }
            material.purchaseSort = this.material.purchaseSort;
            if ("请选择" === this.material.purchaseMark) {
                popoverSpace.append("请选择可采购标记", false);
                return;
            }
            material.purchaseMark = this.material.purchaseMark;
            if ("请选择" === this.material.groupPurMark) {
                popoverSpace.append("请选择可集采标记", false);
                return;
            }
            material.groupPurMark = this.material.groupPurMark;
            if ("请选择" === this.material.ownPurMark) {
                popoverSpace.append("请选择自采标记", false);
                return;
            }
            material.ownPurMark = this.material.ownPurMark;
            if ("" === this.material.fixedAdvTime) {
                popoverSpace.append("请输入固定提前期", false);
                return;
            }
            material.fixedAdvTime = this.material.fixedAdvTime;
            this.saveStart();
            axios.put(requestContext + "api/materials/purchase", material)
                .then(function (response) {
                    let statusCode = response.data.statusCode;
                    if (200 === statusCode) {
                        popoverSpace.append("保存成功", true);
                        main.saveStop();
                    } else {
                        let message = getMessage(statusCode);
                        popoverSpace.append(message, false);
                        main.saveStop();
                    }
                }).catch(function () {
                popoverSpace.append("服务器访问失败", false);
                main.saveStop();
            });
        },
        assemblyCenterSave: function () {
            if ("" === this.material.code) {
                return;
            }
            let material = {};
            material.code = this.material.code;
            if ("请选择" === this.material.defRepository) {
                popoverSpace.append("请选择默认仓库", false);
                return;
            }
            material.defRepository = this.material.defRepository;
            this.saveStart();
            axios.put(requestContext + "api/materials/assemblyCenter", material)
                .then(function (response) {
                    let statusCode = response.data.statusCode;
                    if (200 === statusCode) {
                        popoverSpace.append("保存成功", true);
                        main.saveStop();
                    } else {
                        let message = getMessage(statusCode);
                        popoverSpace.append(message, false);
                        main.saveStop();
                    }
                }).catch(function () {
                popoverSpace.append("服务器访问失败", false);
                main.saveStop();
            });
        },
        produceOperateSave: function () {
            if ("" === this.material.code) {
                return;
            }
            let material = {};
            material.code = this.material.code;
            material.outSource = this.material.outSource;
            if ("" === this.material.planner) {
                popoverSpace.append("请输入计划员", false);
                return;
            }
            material.planner = this.material.planner;
            if ("" === this.material.fixedAdvTime) {
                popoverSpace.append("请输入固定提前期", false);
                return;
            }
            material.fixedAdvTime = this.material.fixedAdvTime;
            this.saveStart();
            axios.put(requestContext + "api/materials/produceOperate", material)
                .then(function (response) {
                    let statusCode = response.data.statusCode;
                    if (200 === statusCode) {
                        popoverSpace.append("保存成功", true);
                        main.saveStop();
                    } else {
                        let message = getMessage(statusCode);
                        popoverSpace.append(message, false);
                        main.saveStop();
                    }
                }).catch(function () {
                popoverSpace.append("服务器访问失败", false);
                main.saveStop();
            });
        },
        setMaterialCompanyNo: function (companyNo) {
            if (null !== companyNo) {
                this.material.companyNo = companyNo;
            }
        },
        setMaterialCode: function (code) {
            if (null !== code) {
                this.material.code = code;
            }
        },
        setMaterialName: function (name) {
            if (null !== name) {
                this.material.name = name;
            }
        },
        setMaterialShortName: function (shortName) {
            if (null !== shortName) {
                this.material.shortName = shortName;
            }
        },
        setMaterialSpecification: function (specification) {
            if (null !== specification) {
                this.material.specification = specification;
            }
        },
        setMaterialModel: function (model) {
            if (null !== model) {
                this.material.model = model;
            }
        },
        setMaterialDescription: function (description) {
            if (null !== description) {
                this.material.description = description;
            }
        },
        setMaterialDrawingNo: function (drawingNo) {
            if (null !== drawingNo) {
                this.material.drawingNo = drawingNo;
            }
        },
        setMaterialGeneralSort: function (generalSort) {
            if (null !== generalSort && "" !== generalSort) {
                this.material.generalSort = generalSort;
            }
        },
        setMaterialInventoryUnit: function (inventoryUnit) {
            if (null !== inventoryUnit && "" !== inventoryUnit) {
                this.material.inventoryUnit = inventoryUnit;
            }
        },
        setMaterialSourceMark: function (sourceMark) {
            if (null !== sourceMark && "" !== sourceMark) {
                this.material.sourceMark = sourceMark;
            }
        },
        setMaterialRespCompany: function (respCompany, respDept) {
            if (null !== respCompany && "" !== respCompany) {
                this.material.respCompany = respCompany;
                this.respDeptSelect();
                this.material.respDept = respDept;
            }
        },
        setMaterialKeyPartMark: function (keyPartMark) {
            if (null === keyPartMark) {
                this.material.keyPartMark = "";
            }
            if (null !== keyPartMark && "" !== keyPartMark) {
                this.material.keyPartMark = keyPartMark;
            }
        },
        setMaterialKeyPartSort: function (keyPartSort) {
            if (null === keyPartSort) {
                this.material.keyPartSort = "";
            }
            if (null !== keyPartSort && "" !== keyPartSort) {
                this.material.keyPartSort = keyPartSort;
            }
        },
        setMaterialVirtualPartMark: function (virtualPartMark) {
            if (null !== virtualPartMark && "" !== virtualPartMark) {
                this.material.virtualPartMark = virtualPartMark;
            }
        },
        setMaterialQualifiedMark: function (qualifiedMark) {
            if (null !== qualifiedMark && "" !== qualifiedMark) {
                this.material.qualifiedMark = qualifiedMark;
            }
        },
        setMaterialInspectMark: function (inspectMark) {
            if (null !== inspectMark && "" !== inspectMark) {
                this.material.inspectMark = inspectMark;
            }
        },
        setMaterialBatchMark: function (batchMark) {
            if (null !== batchMark && "" !== batchMark) {
                this.material.batchMark = batchMark;
            }
        },
        setMaterialPurchaseSort: function (purchaseSort) {
            if (null !== purchaseSort && "" !== purchaseSort) {
                this.material.purchaseSort = purchaseSort;
            }
        },
        setMaterialPurchaseMark: function (purchaseMark) {
            if (null !== purchaseMark && "" !== purchaseMark) {
                this.material.purchaseMark = purchaseMark;
            }
        },
        setMaterialGroupPurMark: function (groupPurMark) {
            if (null !== groupPurMark && "" !== groupPurMark) {
                this.material.groupPurMark = groupPurMark;
            }
        },
        setMaterialOwnPurMark: function (ownPurMark) {
            if (null !== ownPurMark && "" !== ownPurMark) {
                this.material.ownPurMark = ownPurMark;
            }
        },
        setMaterialDefRepository: function (defRepository) {
            if (null !== defRepository && "" !== defRepository) {
                this.material.defRepository = defRepository;
            }
        },
        setMaterialOutSource: function (outSource) {
            if (null !== outSource && "" !== outSource) {
                this.material.outSource = outSource;
            }
        },
        setMaterialPlanner: function (planner) {
            if (null !== planner) {
                this.material.planner = planner;
            }
        },
        setMaterialFixedAdvTime: function (fixedAdvTime) {
            if (null !== fixedAdvTime) {
                this.material.fixedAdvTime = fixedAdvTime;
            }
        }
    },
    mounted: function () {
        this.user = JSON.parse(localStorage.user);
        let url = window.location;
        let code = getUrlParam(url, "materialCode");
        axios.get(requestContext + "api/materials/" + code)
            .then(function (response) {
                let statusCode = response.data.statusCode;
                if (200 === statusCode) {
                    let material = response.data.data;
                    if (null !== material) {
                        main.setMaterialCompanyNo(material.companyNo);
                        main.setMaterialCode(material.code);
                        main.setMaterialName(material.name);
                        main.setMaterialShortName(material.shortName);
                        main.setMaterialSpecification(material.specification);
                        main.setMaterialModel(material.model);
                        main.setMaterialDescription(material.description);
                        main.setMaterialDrawingNo(material.drawingNo);
                        main.setMaterialGeneralSort(material.generalSort);
                        main.setMaterialInventoryUnit(material.inventoryUnit);
                        main.setMaterialSourceMark(material.sourceMark);
                        main.setMaterialRespCompany(material.respCompany, material.respDept);
                        main.setMaterialKeyPartMark(material.keyPartMark);
                        main.setMaterialKeyPartSort(material.keyPartSort);
                        main.setMaterialVirtualPartMark(material.virtualPartMark);
                        main.setMaterialQualifiedMark(material.qualifiedMark);
                        main.setMaterialInspectMark(material.inspectMark);
                        main.setMaterialBatchMark(material.batchMark);
                        main.setMaterialPurchaseSort(material.purchaseSort);
                        main.setMaterialPurchaseMark(material.purchaseMark);
                        main.setMaterialGroupPurMark(material.groupPurMark);
                        main.setMaterialOwnPurMark(material.ownPurMark);
                        main.setMaterialDefRepository(material.defRepository);
                        main.setMaterialOutSource(material.outSource);
                        main.setMaterialPlanner(material.planner);
                        main.setMaterialFixedAdvTime(material.fixedAdvTime);
                    } else {
                        popoverSpace.append("数据获取失败", false);
                    }
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