package top.mikecao.wager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.mikecao.wager.common.KeyGen;

@Configuration
public class IdConfig {

    @Bean
    public KeyGen customKeyGen(){
        return new KeyGen();
    }

}
