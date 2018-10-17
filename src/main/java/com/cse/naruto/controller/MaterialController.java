package com.cse.naruto.controller;

import com.cse.naruto.model.Material;
import com.cse.naruto.model.Response;
import com.cse.naruto.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 物料数据CRUD API
 *
 * @author 王振琦
 * createAt 2018/10/16
 * updateAt 2018/10/16
 */
@CrossOrigin
@RestController
@RequestMapping(value = "api")
public class MaterialController extends NarutoFacade {

    private final MaterialService materialService;

    @Autowired
    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PutMapping(value = "materials/technologyCenter")
    public Response<Material> actionUpdateMaterialByTechnologyCenter(@RequestBody Material material) {
        materialService.updateMaterialByTechnologyCenter(material);
        return new Response<>();
    }

    @PutMapping(value = "materials/qualifiedEnvironment")
    public Response<Material> actionUpdateMaterialByQualifiedEnvironment(@RequestBody Material material) {
        materialService.updateMaterialByQualifiedEnvironment(material);
        return new Response<>();
    }

    @PutMapping(value = "materials/purchase")
    public Response<Material> actionUpdateMaterialByPurchase(@RequestBody Material material) {
        materialService.updateMaterialByPurchase(material);
        return new Response<>();
    }

    @PutMapping(value = "materials/assemblyCenter")
    public Response<Material> actionUpdateMaterialByAssemblyCenter(@RequestBody Material material) {
        materialService.updateMaterialByAssemblyCenter(material);
        return new Response<>();
    }

    @PutMapping(value = "materials/produceOperate")
    public Response<Material> actionUpdateMaterialByProduceOperate(@RequestBody Material material) {
        materialService.updateMaterialByProduceOperate(material);
        return new Response<>();
    }

    @GetMapping(value = "materials/{code}")
    public Response<Material> actionQueryMaterialByCode(@PathVariable(value = "code") String code) {
        Material material = materialService.findMaterialByCode(code);
        return new Response<>(material);
    }
}
