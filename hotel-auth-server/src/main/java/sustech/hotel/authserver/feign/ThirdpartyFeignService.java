package sustech.hotel.authserver.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sustech.hotel.common.utils.JsonResult;

@FeignClient("hotel-thirdparty")
public interface ThirdpartyFeignService {
    @GetMapping("/sms/sendcode")
    JsonResult<Void> sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code);
}
