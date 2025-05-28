package zip.hyeon.snslogintemplate.api;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import zip.hyeon.snslogintemplate.exception.example.CommonResponse;
import zip.hyeon.snslogintemplate.exception.example.GlobalException;
import zip.hyeon.snslogintemplate.exception.example.ResultCode;
import zip.hyeon.snslogintemplate.security.jwt.dto.CustomUserDetails;

@RestController
public class UserController {

    @GetMapping("/auth")
    public String test(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        System.out.println("customUserDetails.getUserId() : " + customUserDetails.getUserId());
        return "loginSuccess";
    }

    @GetMapping("/test")
    public CommonResponse<?> ttee(){

        throw new GlobalException(ResultCode.INVALID_REGISTRATION_ID);

    }


}
