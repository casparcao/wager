package top.mikecao.wager.config.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Key;
import java.util.Collections;

public class JwtTokenVerifier extends GenericFilterBean {

    public static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String path = req.getServletPath();
        if(path.equals("/api/token") || path.equals("/error") || path.equals("/api/sign/up")){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String jwtToken = req.getHeader("authorization");
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(jwtToken.replace("Bearer",""))
                .getBody();
        String idStr = (String)claims.get("id");
        long id = Long.parseLong(idStr);
        String username = claims.getSubject();//获取当前登录用户名
        Who ur = new Who();
        ur.setId(id);
        ur.setUsername(username);
//        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(ur, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(token);
        filterChain.doFilter(req,servletResponse);
    }
}
