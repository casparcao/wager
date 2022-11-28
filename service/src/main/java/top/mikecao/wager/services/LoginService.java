package top.mikecao.wager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.mikecao.wager.entities.User;
import top.mikecao.wager.repositories.UserRepository;
import top.mikecao.wager.config.auth.Who;

import java.util.Collections;
import java.util.Optional;

@Service
public class LoginService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opt = userRepository.findByUsername(username);
        if(opt.isEmpty()){
            throw new UsernameNotFoundException("用户名不存在：" + username);
        }
        Who result = new Who();
        result.setId(opt.get().getId())
                .setUsername(opt.get().getUsername())
                .setPassword(opt.get().getPassword())
                .setRoles(Collections.emptyList());
        return result;
    }

}
