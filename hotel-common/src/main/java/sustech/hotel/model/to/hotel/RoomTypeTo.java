package sustech.hotel.model.to.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeTo {
    private Long typeId;
    private Long hotelId;
    private Integer upperLimit;
    private BigDecimal price;
    private Integer breakfast;
    private Integer windows;
    private Integer television;
    private Integer bathtub;
    private Integer thermos;
    private String typeName;
    private String description;
    private BigDecimal area;
}
