package top.mikecao.wager.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import top.mikecao.wager.common.Result;
import top.mikecao.wager.vo.LoginRequest;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

import static top.mikecao.wager.config.auth.JwtTokenVerifier.KEY;

@Slf4j
public class CustomJwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    protected CustomJwtAuthenticationFilter(String defaultFilterProcessesUrl,
                                            AuthenticationManager authenticationManager,
                                            ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl, HttpMethod.POST.name()));
        setAuthenticationManager(authenticationManager);
        this.objectMapper = objectMapper;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse resp)
            throws AuthenticationException{
        LoginRequest user;
        try {
            user = objectMapper.readValue(req.getInputStream(), LoginRequest.class);
        }catch (Exception e){
            log.info("无登录信息>>", e);
            throw new BadCredentialsException("无登录信息");
        }
        return getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse resp,
                                            FilterChain chain,
                                            Authentication auth) throws IOException{
        if(Objects.isNull(auth) || Objects.isNull(auth.getPrincipal())){
            logger.error("认证失败");
            return;
        }
        Object principal = auth.getPrincipal();
        if(!(principal instanceof Who)){
            logger.error("认证失败2");
            return;
        }
        Who ur = (Who) principal;
        String jwt = Jwts.builder()
                .setSubject(auth.getName())
                .setExpiration(Date.from(Instant.now().plusSeconds(6 * 60 * 60)))
                .claim("id", ur.getId()+"")
                .signWith(KEY)
                .compact();
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.write(objectMapper.writeValueAsString(Result.ok(jwt)));
        out.flush();
        out.close();
    }
    protected void unsuccessfulAuthentication(HttpServletRequest req,
                                              HttpServletResponse resp,
                                              AuthenticationException failed) throws IOException{
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.write(objectMapper.writeValueAsString(Result.fail("用户名或密码错误")));
        out.flush();
        out.close();
    }

}
