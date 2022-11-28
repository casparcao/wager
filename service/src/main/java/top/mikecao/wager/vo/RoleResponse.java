package top.mikecao.wager.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;

@Data
@Accessors(chain = true)
public class RoleResponse implements GrantedAuthority {
    private String role;

    @Override
    public String getAuthority() {
        return role;
    }
}
