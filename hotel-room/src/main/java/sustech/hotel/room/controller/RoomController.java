package sustech.hotel.room.controller;

import java.util.*;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import sustech.hotel.exception.ExceptionCodeEnum;
import sustech.hotel.exception.room.RoomExistedException;
import sustech.hotel.model.to.hotel.RoomInfoTo;
import sustech.hotel.model.to.order.OrderTo;
import sustech.hotel.model.vo.hotel.BookingRoomInfoVo;
import sustech.hotel.model.vo.order.OrderShowVo;
import sustech.hotel.room.dao.RoomDao;
import sustech.hotel.model.to.order.OrderInfoTo;
import sustech.hotel.room.entity.HotelEntity;
import sustech.hotel.room.entity.LayoutEntity;
import sustech.hotel.room.entity.RoomEntity;
import sustech.hotel.room.entity.RoomTypeEntity;
import sustech.hotel.room.service.HotelService;
import sustech.hotel.room.service.LayoutService;
import sustech.hotel.room.service.RoomService;
import sustech.hotel.common.utils.PageUtils;
import sustech.hotel.common.utils.JsonResult;
import sustech.hotel.room.service.RoomTypeService;

@Api("房间控制类")
@RestController
@RequestMapping("room/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private LayoutService layoutService;

    @Autowired
    private RoomDao roomDao;

    @RequestMapping("/allInfo/{roomId}")
    public JsonResult<RoomInfoTo> allInfo(@PathVariable("roomId") Long roomId) {
        RoomInfoTo roomInfoTo = new RoomInfoTo();
        RoomEntity room = roomService.getById(roomId);
        RoomTypeEntity roomType = roomTypeService.getById(room.getTypeId());
        HotelEntity hotel = hotelService.getById(room.getHotelId());
        BeanUtils.copyProperties(room, roomInfoTo);
        BeanUtils.copyProperties(roomType, roomInfoTo);
        BeanUtils.copyProperties(hotel, roomInfoTo);
        return new JsonResult<>(roomInfoTo);
    }

    @RequestMapping("/orderinfo/{roomId}")
    public JsonResult<OrderInfoTo> getOrderInfo(@PathVariable("roomId") Long roomId) {
        RoomEntity room = roomService.getById(roomId);
        Long hotelId = room.getHotelId();
        HotelEntity hotel = hotelService.getById(hotelId);
        Long roomNumber = room.getRoomNumber();
        String hotelName = hotel.getHotelName();
        OrderInfoTo to = new OrderInfoTo();
        to.setRoomNumber(roomNumber);
        to.setHotelName(hotelName);
        return new JsonResult<>(to);
    }

    /**
     * 根据传入的参数map进行分页查询
     */
    @RequestMapping("/list")
    public JsonResult<PageUtils> list(@RequestParam Map<String, Object> params) {
        PageUtils page = roomService.queryPage(params);
        return new JsonResult<>(page);
    }


    /**
     * 查找数据表中的一条数据（根据主键查找）
     */
    @RequestMapping("/info/{roomId}")
    public JsonResult<RoomEntity> info(@PathVariable("roomId") Long roomId) {
        RoomEntity room = roomService.getById(roomId);
        return new JsonResult<>(room);
    }


    /**
     * 保存一条数据到数据库中
     */
    @RequestMapping("/save")
    public JsonResult<Void> save(@RequestBody RoomEntity room) {
        roomService.save(room);
        return new JsonResult<>();
    }

    /**
     * 修改数据库中的一条数据（根据传入的一条类数据）
     */
    @RequestMapping("/update")
    public JsonResult<Void> update(@RequestBody RoomEntity room) {
        roomService.updateById(room);
        return new JsonResult<>();
    }

    /**
     * 批量删除数据库中的数据（根据主键删除）
     */
    @RequestMapping("/delete")
    public JsonResult<Void> delete(@RequestBody Long[] roomIds) {
        roomService.removeByIds(Arrays.asList(roomIds));
        return new JsonResult<>();
    }

    @GetMapping("/availableRoom")
    public JsonResult<List<Long>> getAvailableRoom(@RequestParam("hotel_id") Long hotelId, @RequestParam("type_id") Long typeId, @RequestParam("json") String json) {
        List<Long> conflictList = JSON.parseArray(json, Long.class);
        List<Long> list = roomService.getAvailableRoom(hotelId, typeId, conflictList);
        return new JsonResult<>(list);
    }

    @GetMapping("/floorRoomList")
    public JsonResult<List<BookingRoomInfoVo>> getFloorRoomList(@RequestParam("hotel_id") Long hotelId, @RequestParam("floor") Long floor) {
        LayoutEntity layout = layoutService.getOne(new QueryWrapper<LayoutEntity>().and(o -> o.eq("hotel_id", hotelId).eq("floor", floor)));
        return new JsonResult<>(roomDao.selectRoomByHotelIdAndLayoutId(hotelId, layout.getLayoutId()));
    }

    @ResponseBody
    @RequestMapping("/getOrderRoomInfo")
    public JsonResult<String> getOrderRoomInfo(@RequestParam("json") String json) {
        List<OrderTo> list = JSON.parseArray(json, OrderTo.class);
        List<OrderShowVo> vos;
        if (list != null) {
            vos = list.stream().map(o -> {
                OrderShowVo vo = new OrderShowVo();
                Long hotelId = o.getHotelId();
                Long typeId = o.getTypeId();
                Long roomId = o.getRoomId();
                HotelEntity hotel = hotelService.getById(hotelId);
                RoomTypeEntity roomType = roomTypeService.getById(typeId);
                RoomEntity room = roomService.getById(roomId);
                vo.setHotelName(hotel.getHotelName());
                vo.setTypeName(roomType.getTypeName());
                vo.setRoomNumber(room.getRoomNumber());
                BeanUtils.copyProperties(o, vo);
                return vo;
            }).toList();
        } else vos = new ArrayList<>();
        String s = JSON.toJSONString(vos);
        return new JsonResult<>(s);
    }

    @ResponseBody
    @GetMapping("/addRoom")
    public JsonResult<Void> addRoom(@RequestParam("hotelId") Long hotelId, @RequestParam("roomNumber") Long roomNumber, @RequestParam("roomType") String roomType,
                                    @RequestParam("floor") Long floor, @RequestParam("floorPlan") Long floorPlan) {
        RoomEntity room = new RoomEntity();
        List<RoomEntity> rooms = roomService.list(new QueryWrapper<RoomEntity>().eq("hotel_id", hotelId));
        List<RoomEntity> list = rooms.stream().filter(o -> Objects.equals(o.getRoomNumber(), roomNumber)).toList();
        if (!list.isEmpty()) {
            return new JsonResult<>(new RoomExistedException(ExceptionCodeEnum.ROOM_EXISTED_EXCEPTION));
        }
        LayoutEntity layout = layoutService.getOne(new QueryWrapper<LayoutEntity>().and(o -> o.eq("hotel_id", hotelId).eq("floor", floor)));
        Long layoutId = layout.getLayoutId();
        List<RoomEntity> list1 = roomService.list(new QueryWrapper<RoomEntity>().and(o -> o.eq("layout_id", layoutId).eq("floor_plan_id", floorPlan)));
        if (!list1.isEmpty()) {
            return new JsonResult<>(new RoomExistedException(ExceptionCodeEnum.ROOM_EXISTED_EXCEPTION));
        }
        RoomTypeEntity type = roomTypeService.getOne(new QueryWrapper<RoomTypeEntity>().and(o -> o.eq("type_name", roomType).eq("hotel_id", hotelId)));
        room.setLayoutId(layoutId);
        room.setRoomNumber(roomNumber);
        room.setFloorPlanId(floorPlan);
        room.setHotelId(hotelId);
        room.setTypeId(type.getTypeId());
        this.save(room);
        return new JsonResult<>();
    }

    @ResponseBody
    @GetMapping("/editRoom")
    public JsonResult<Void> editRoom(@RequestParam("hotelId") Long hotelId, @RequestParam("roomNumber") Long roomNumber, @RequestParam("roomType") String roomType,
                                     @RequestParam("floor") Long floor, @RequestParam("floorPlan") Long floorPlan, @RequestParam("oldFloor") Long oldFloor,
                                     @RequestParam("oldFloorPlan") Long oldFloorPlan) {
        LayoutEntity layout = layoutService.getOne(new QueryWrapper<LayoutEntity>().and(o -> o.eq("hotel_id", hotelId).eq("floor", oldFloor)));
        Long layoutId = layout.getLayoutId();
        RoomEntity originRoom = roomService.getOne(new QueryWrapper<RoomEntity>()
                .and(o -> o.eq("hotel_id", hotelId)
                        .eq("layout_id", layoutId)
                        .eq("floor_plan_id", oldFloorPlan)));
        RoomEntity byRoomNumber = roomService.getOne(new QueryWrapper<RoomEntity>().and(o -> o.eq("room_number", roomNumber).eq("hotel_id", hotelId)));
        if (byRoomNumber != null && !originRoom.equals(byRoomNumber)) {
            return new JsonResult<>(new RoomExistedException(ExceptionCodeEnum.ROOM_EXISTED_EXCEPTION));
        }
        LayoutEntity newLayout = layoutService.getOne(new QueryWrapper<LayoutEntity>().and(o -> o.eq("hotel_id", hotelId).eq("floor", floor)));
        Long newLayoutId = newLayout.getLayoutId();
        RoomEntity byLocation = roomService.getOne(new QueryWrapper<RoomEntity>()
                .and(o -> o.eq("hotel_id", hotelId)
                        .eq("layout_id", newLayoutId)
                        .eq("floor_plan_id", floorPlan)));
        if (byLocation != null && !originRoom.equals(byLocation)) {
            return new JsonResult<>(new RoomExistedException(ExceptionCodeEnum.ROOM_EXISTED_EXCEPTION));
        }
        RoomTypeEntity type = roomTypeService.getOne(new QueryWrapper<RoomTypeEntity>().and(o -> o.eq("type_name", roomType).eq("hotel_id", hotelId)));
        originRoom.setRoomNumber(roomNumber);
        originRoom.setTypeId(type.getTypeId());
        originRoom.setLayoutId(newLayoutId);
        originRoom.setFloorPlanId(floorPlan);
        this.roomService.updateById(originRoom);
        return new JsonResult<>();
    }

    @ResponseBody
    @GetMapping("/deleteRoom")
    public JsonResult<Void> deleteRoom(@RequestParam("hotelId") Long hotelId, @RequestParam("roomNumber") Long roomNumber, @RequestParam("roomType") String roomType,
                                       @RequestParam("floor") Long floor, @RequestParam("floorPlan") Long floorPlan) {
        System.out.println(roomNumber);
        System.out.println(hotelId);
        roomService.remove(new QueryWrapper<RoomEntity>()
                .and(o -> o.eq("room_number", roomNumber)
                        .eq("hotel_id", hotelId)));
        return new JsonResult<>();
    }

}