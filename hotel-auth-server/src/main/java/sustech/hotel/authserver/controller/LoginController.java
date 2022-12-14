package sustech.hotel.authserver.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import sustech.hotel.authserver.feign.MemberFeignService;
import sustech.hotel.authserver.feign.ThirdpartyFeignService;
import sustech.hotel.common.utils.JsonResult;
import sustech.hotel.constant.AuthConstant;
import sustech.hotel.exception.ExceptionCodeEnum;
import sustech.hotel.exception.auth.SmsCodeHighFrequencyException;
import sustech.hotel.exception.auth.SmsCodeIncorrectException;
import sustech.hotel.model.vo.member.CodeLoginVo;
import sustech.hotel.model.vo.member.PasswordLoginVo;
import sustech.hotel.model.vo.member.PhoneVo;
import sustech.hotel.model.vo.member.UserRespVo;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static sustech.hotel.common.utils.Constant.OK;

@Controller
public class LoginController {
    @Autowired
    MemberFeignService memberFeignService;

    @Autowired
    ThirdpartyFeignService thirdpartyFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Operation(summary = "发送登录短信验证码接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true)
    })
    @ResponseBody
    @GetMapping("/message/login")
    public JsonResult<Void> message(PhoneVo vo) {
        String redisCode = redisTemplate.opsForValue().get(AuthConstant.SMS_CODE_CACHE_LOGIN_PREFIX + vo.getPhone());
        if (redisCode != null) {
            //设定发送短信间隔时长1min
            long l = Long.parseLong(redisCode.substring(7));
            if (System.currentTimeMillis() - l < 60000) {
                return new JsonResult<>(new SmsCodeHighFrequencyException(ExceptionCodeEnum.SMS_CODE_HIGH_FREQUENCY_EXCEPTION));
            }
        }
        //随机生成验证码
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        //调用第三方服务发送短信
        JsonResult<Void> result = thirdpartyFeignService.sendCode(vo.getPhone(), String.valueOf(code));
        if (result.getState() != OK) {
            return result;
        }
        code.append("_").append(System.currentTimeMillis());
        //验证码有效期三分钟
        redisTemplate.opsForValue().set(AuthConstant.SMS_CODE_CACHE_LOGIN_PREFIX + vo.getPhone(),
                String.valueOf(code), 3, TimeUnit.MINUTES);
        return result;
    }

    @Operation(summary = "根据手机号和密码登录")
    @ResponseBody
    @PostMapping("/login/password")
    public JsonResult<UserRespVo> loginByPassword(@RequestBody @Valid PasswordLoginVo vo, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField,
                    o -> Optional.ofNullable(o.getDefaultMessage()).orElse("")));
            return new JsonResult<>(ExceptionCodeEnum.INVALID_INPUT_EXCEPTION.getCode(),
                    ExceptionCodeEnum.INVALID_INPUT_EXCEPTION.getMessage(), errors);
        }
        return memberFeignService.loginByPassword(vo);
    }

    @Operation(summary = "根据手机号和验证码登录")
    @ResponseBody
    @PostMapping("/login/code")
    public JsonResult<UserRespVo> loginByCode(@RequestBody CodeLoginVo vo) {
        String code = vo.getCode();
        String redisCode = redisTemplate.opsForValue().get(AuthConstant.SMS_CODE_CACHE_LOGIN_PREFIX + vo.getPhone());
        if (!StringUtils.isEmpty(redisCode)) {
            if (code.equals(redisCode.substring(0, 6))) {
                redisTemplate.delete(AuthConstant.SMS_CODE_CACHE_LOGIN_PREFIX + vo.getPhone());
                return memberFeignService.loginByCode(vo.getPhone());
            }
        }
        return new JsonResult<>(new SmsCodeIncorrectException(ExceptionCodeEnum.SMS_CODE_INCORRECT_EXCEPTION));
    }
}
