package sustech.hotel.discount.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sustech.hotel.discount.entity.PromotionEntity;
import sustech.hotel.discount.service.PromotionService;
import sustech.hotel.common.utils.Constant;
import sustech.hotel.common.utils.PageUtils;
import sustech.hotel.common.utils.JsonResult;


@RestController
@RequestMapping("discount/promotion")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    /**
     * 根据传入的参数map进行分页查询
     */
    @RequestMapping("/list")
    public JsonResult<PageUtils> list(@RequestParam Map<String, Object> params){
        PageUtils page = promotionService.queryPage(params);
        return new JsonResult<>(page);
    }

    /**
     * 查找数据表中的一条数据（根据主键查找）
     */
    @RequestMapping("/info/{promotionId}")
    public JsonResult<PromotionEntity> info(@PathVariable("promotionId") Integer promotionId){
		PromotionEntity promotion = promotionService.getById(promotionId);
        return new JsonResult<>(promotion);
    }

    /**
     * 保存一条数据到数据库中
     */
    @RequestMapping("/save")
    public JsonResult<Void> save(@RequestBody PromotionEntity promotion){
		promotionService.save(promotion);
        return new JsonResult<>();
    }

    /**
     * 修改数据库中的一条数据（根据传入的一条类数据）
     */
    @RequestMapping("/update")
    public JsonResult<Void> update(@RequestBody PromotionEntity promotion){
		promotionService.updateById(promotion);
        return new JsonResult<>();
    }

    /**
     * 批量删除数据库中的数据（根据主键删除）
     */
    @RequestMapping("/delete")
    public JsonResult<Void> delete(@RequestBody Integer[] promotionIds){
		promotionService.removeByIds(Arrays.asList(promotionIds));
        return new JsonResult<>();
    }
}