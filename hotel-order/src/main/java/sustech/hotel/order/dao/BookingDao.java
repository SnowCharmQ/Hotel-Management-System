package sustech.hotel.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import sustech.hotel.order.entity.BookingEntity;

import java.sql.Date;
import java.util.List;

@Mapper
public interface BookingDao extends BaseMapper<BookingEntity> {
    List<Long> selectTypeByTimeIntervalAndHotel(@Param("start_date") Date startDate, @Param("end_date") Date endDate, @Param("hotel_id") Long hotelId);
    List<Long> selectConflictRoomByTimeIntervalAndHotel(@Param("start_date") Date startDate, @Param("end_date") Date endDate, @Param("hotel_id") Long hotelId);
}