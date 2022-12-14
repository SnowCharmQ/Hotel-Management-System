package sustech.hotel.room.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName("chr_admin")
public class AdminEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long hotelId;
    private String password;
}