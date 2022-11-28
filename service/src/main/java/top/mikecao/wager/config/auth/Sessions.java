package top.mikecao.wager.config.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public final class Sessions {

    public static Who current(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Who anonymous = new Who();
        if(Objects.isNull(authentication)){
            return anonymous;
        }
        Object principal = authentication.getPrincipal();
        if (Objects.isNull(principal) || !(principal instanceof Who)) {
            return anonymous;
        }
        return (Who) principal;
    }
}
