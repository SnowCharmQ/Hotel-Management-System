package sustech.hotel.room.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sustech.hotel.common.utils.JsonResult;
import sustech.hotel.common.utils.PageUtils;
import sustech.hotel.exception.ExceptionCodeEnum;
import sustech.hotel.exception.order.HotelNotFoundException;
import sustech.hotel.model.vo.hotel.HotelVo;
import sustech.hotel.model.vo.hotel.ReserveReqVo;
import sustech.hotel.model.vo.hotel.ReserveRespVo;
import sustech.hotel.model.vo.hotel.SearchRespVo;
import sustech.hotel.room.entity.HotelEntity;
import sustech.hotel.room.entity.HotelPictureEntity;
import sustech.hotel.room.service.HotelPictureService;
import sustech.hotel.room.service.HotelService;

import java.math.BigDecimal;
import java.util.*;


@RestController
@RequestMapping("room/hotel")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private HotelPictureService hotelPictureService;

    @ResponseBody
    @GetMapping("/search/hotel")
    public JsonResult<SearchRespVo> searchHotel(@RequestParam("token") String token, @RequestParam("sortBy") String sortBy,
                                                @RequestParam("reversed") Boolean reversed, @RequestParam("diningRoom") Boolean diningRoom,
                                                @RequestParam("parking") Boolean parking, @RequestParam("spa") Boolean spa,
                                                @RequestParam("bar") Boolean bar, @RequestParam("gym") Boolean gym,
                                                @RequestParam("chessRoom") Boolean chessRoom, @RequestParam("swimmingPool") Boolean swimmingPool,
                                                @RequestParam("lowest") BigDecimal lowest, @RequestParam("highest") BigDecimal highest) {
        SearchRespVo vo = hotelService.searchHotel(token, sortBy, reversed, diningRoom, parking, spa, bar, gym, chessRoom, swimmingPool, lowest, highest);
        return new JsonResult<>(vo);
    }

    @ResponseBody
    @GetMapping("/initSearch")
    public JsonResult<SearchRespVo> initSearch(@RequestParam("token") String token) {
        SearchRespVo respVo = hotelService.initSearch(token);
        return new JsonResult<>(respVo);
    }

    @ResponseBody
    @GetMapping("/initReserve")
    public JsonResult<ReserveRespVo> initReserve(ReserveReqVo vo) {
        ReserveRespVo resp = new ReserveRespVo();
        HotelEntity entity = hotelService.getById(vo.getHotelId());
        if (entity == null) {
            return new JsonResult<>(new HotelNotFoundException(ExceptionCodeEnum.HOTEL_NOT_FOUND_EXCEPTION));
        }
        List<HotelPictureEntity> pictures
                = hotelPictureService.list(new QueryWrapper<HotelPictureEntity>().eq("hotel_id", vo.getHotelId()));
        List<String> images = pictures.stream().map(HotelPictureEntity::getPicturePath).toList();
        resp.setImages(images);
        BeanUtils.copyProperties(entity, resp);
        return new JsonResult<>(resp);
    }

    /**
     * 根据传入的参数map进行分页查询
     */
    @RequestMapping("/list")
    public JsonResult<PageUtils> list(@RequestParam Map<String, Object> params) {
        PageUtils page = hotelService.queryPage(params);
        return new JsonResult<>(page);
    }


    /**
     * 查找数据表中的一条数据（根据主键查找）
     */
    @RequestMapping("/info/{hotelId}")
    public JsonResult<HotelEntity> info(@PathVariable("hotelId") Long hotelId) {
        HotelEntity hotel = hotelService.getById(hotelId);
        return new JsonResult<>(hotel);
    }

    /**
     * 保存一条数据到数据库中
     */
    @RequestMapping("/save")
    public JsonResult<Void> save(@RequestBody HotelEntity hotel) {
        hotelService.save(hotel);
        return new JsonResult<>();
    }

    /**
     * 修改数据库中的一条数据（根据传入的一条类数据）
     */
    @RequestMapping("/update")
    public JsonResult<Void> update(@RequestBody HotelEntity hotel) {
        hotelService.updateById(hotel);
        return new JsonResult<>();
    }

    /**
     * 批量删除数据库中的数据（根据主键删除）
     */
    @RequestMapping("/delete")
    public JsonResult<Void> delete(@RequestBody Long[] hotelIds) {
        hotelService.removeByIds(Arrays.asList(hotelIds));
        return new JsonResult<>();
    }
}