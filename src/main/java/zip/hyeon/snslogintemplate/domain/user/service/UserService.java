package zip.hyeon.snslogintemplate.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zip.hyeon.snslogintemplate.domain.user.entity.UserEntity;
import zip.hyeon.snslogintemplate.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity getReferenceById(Long userId){
        return userRepository.getReferenceById(userId);
    }
}
