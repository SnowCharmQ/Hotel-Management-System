package sustech.hotel.member.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sustech.hotel.common.utils.Constant;
import sustech.hotel.member.entity.UserInfoEntity;
import sustech.hotel.member.service.UserInfoService;
import sustech.hotel.common.utils.PageUtils;
import sustech.hotel.common.utils.JsonResult;


@RestController
@RequestMapping("member/userinfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 根据传入的参数map进行分页查询
     */
    @RequestMapping("/list")
    public JsonResult<PageUtils> list(@RequestParam Map<String, Object> params){
        PageUtils page = userInfoService.queryPage(params);
        return new JsonResult<>(Constant.OK, page);
    }


    /**
     * 查找数据表中的一条数据（根据主键查找）
     */
    @RequestMapping("/info/{userId}")
    public JsonResult<UserInfoEntity> info(@PathVariable("userId") Long userId){
		UserInfoEntity userInfo = userInfoService.getById(userId);
        return new JsonResult<>(Constant.OK, userInfo);
    }

    /**
     * 保存一条数据到数据库中
     */
    @RequestMapping("/save")
    public JsonResult<Void> save(@RequestBody UserInfoEntity userInfo){
		userInfoService.save(userInfo);
        return new JsonResult<>(Constant.OK);
    }

    /**
     * 修改数据库中的一条数据（根据传入的一条类数据）
     */
    @RequestMapping("/update")
    public JsonResult<Void> update(@RequestBody UserInfoEntity userInfo){
		userInfoService.updateById(userInfo);
        return new JsonResult<>(Constant.OK);
    }

    /**
     * 批量删除数据库中的数据（根据主键删除）
     */
    @RequestMapping("/delete")
    public JsonResult<Void> delete(@RequestBody Long[] userIds){
		userInfoService.removeByIds(Arrays.asList(userIds));
        return new JsonResult<>(Constant.OK);
    }
}