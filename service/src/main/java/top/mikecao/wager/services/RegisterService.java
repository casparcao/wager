package top.mikecao.wager.services;

import com.mongodb.MongoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.mikecao.wager.common.KeyGen;
import top.mikecao.wager.common.Result;
import top.mikecao.wager.entities.User;
import top.mikecao.wager.repositories.UserRepository;
import top.mikecao.wager.vo.SignUpRequest;

@Slf4j
@Service
public class RegisterService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private KeyGen key;

    public Result<Void> action(SignUpRequest request) {
        User user = new User();
        user.setId(key.next())
                .setUsername(request.getUsername())
                .setPassword(passwordEncoder.encode(request.getPassword()));
        try {
            userRepository.save(user);
        }catch (MongoException e){
            log.info("名称已存在唯一键冲突>>{}", request.getUsername());
        }
        return Result.ok();
    }
}
