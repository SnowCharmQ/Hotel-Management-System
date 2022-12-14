package sustech.hotel.model.to.order;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderTo {
    private String orderId;
    private Long userId;
    private Long typeId;
    private Long roomId;
    private Integer orderStatus;
    private Date startDate;
    private Date endDate;
    private BigDecimal originMoney;
    private BigDecimal afterDiscount;
    private String additional;
    private Integer score;
    private Long hotelId;
    private String contactName;
    private String contactPhone;
    private String contactEmail;
}
