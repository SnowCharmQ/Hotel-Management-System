package sustech.hotel.room.service;

import com.baomidou.mybatisplus.extension.service.IService;
import sustech.hotel.common.utils.PageUtils;
import sustech.hotel.model.vo.hotel.HotelMapVo;
import sustech.hotel.model.vo.hotel.ReserveReqVo;
import sustech.hotel.model.vo.hotel.ReserveRespVo;
import sustech.hotel.model.vo.hotel.SearchRespVo;
import sustech.hotel.room.entity.HotelEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface HotelService extends IService<HotelEntity> {
    PageUtils queryPage(Map<String, Object> params);

    SearchRespVo initSearch(String token);

    SearchRespVo searchHotel(String token, String sortBy, Boolean reversed, Boolean diningRoom,
                             Boolean parking, Boolean spa, Boolean bar, Boolean gym, Boolean chessRoom,
                             Boolean swimmingPool, BigDecimal lowest, BigDecimal highest, String location);

    ReserveRespVo initReserve(ReserveReqVo vo);

    public List<HotelMapVo> getMapInfo();

    HotelEntity getHotelByName(String hotel);
}