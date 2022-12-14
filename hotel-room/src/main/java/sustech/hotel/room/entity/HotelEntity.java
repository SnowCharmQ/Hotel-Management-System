package sustech.hotel.room.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
@TableName("chr_hotel")
public class HotelEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId
    private Long hotelId;
    private String province;
    private String city;
    private String district;
    private String hotelName;
    private String detailAddress;
    private String email;
    private String description;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String telephone;
    private Integer swimmingPool;
    private Integer gym;
    private Integer diningRoom;
    private Integer bar;
    private Integer parking;
    private Integer spa;
    private Integer chessRoom;
    private Long starLevel;
    private Integer floors;
}