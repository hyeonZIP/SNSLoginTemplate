package zip.hyeon.snslogintemplate.api;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import zip.hyeon.snslogintemplate.security.jwt.dto.CustomUserDetails;

@RestController
public class UserApiController {

    @GetMapping(("/login-success"))
    public void test(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        System.out.println("customUserDetails.getUserId() : " + customUserDetails.getUserId());
        System.out.println("customUserDetails.getUserRole() : " + customUserDetails.getUserRole());
    }
}
