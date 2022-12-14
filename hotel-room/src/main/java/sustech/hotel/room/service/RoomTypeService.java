package sustech.hotel.room.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import sustech.hotel.common.utils.PageUtils;
import sustech.hotel.exception.ExceptionCodeEnum;
import sustech.hotel.exception.room.RoomConflictsException;
import sustech.hotel.model.to.hotel.AvailableRoomTypeTo;
import sustech.hotel.model.to.hotel.CommentInfoTo;
import sustech.hotel.model.vo.hotel.RoomTypeInfoVo;
import sustech.hotel.model.vo.hotel.RoomTypeSearchVo;
import sustech.hotel.model.vo.hotel.RoomTypeVo;
import sustech.hotel.room.entity.RoomTypeEntity;
import sustech.hotel.room.entity.RoomTypePictureEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RoomTypeService extends IService<RoomTypeEntity> {
    PageUtils queryPage(Map<String, Object> params);

    List<AvailableRoomTypeTo> getAvailableRoomType(Long hotelId, List<Long> conflictList);

    RoomTypeSearchVo search(Long hotelId, Integer guests, String startDate, String endDate, String sortBy, Boolean reversed, BigDecimal lowest, BigDecimal highest, Boolean breakfast, Boolean windows, Boolean television, Boolean bathtub, Boolean thermos);

    CommentInfoTo getCommentInfo(Long typeId);

    Long addRoomType(RoomTypeVo entity);

    void deleteType(Long typeId);

    void alterType(RoomTypeEntity roomType);

    List<RoomTypeInfoVo> getRoomType(String hotel);


}