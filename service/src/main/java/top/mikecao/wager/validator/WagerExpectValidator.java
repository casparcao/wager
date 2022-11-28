package top.mikecao.wager.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.mikecao.wager.entities.Rule;
import top.mikecao.wager.exception.AppClientException;
import top.mikecao.wager.vo.WagerRequest;
import top.mikecao.wager.vo.expect.BaseWagerExpect;

@Slf4j
@Service
public class WagerExpectValidator {

    @Autowired
    private ObjectMapper objectMapper;

    public BaseWagerExpect validate(WagerRequest request) {
        Rule rule = request.getRule();
        String expect = request.getExpect();
        Class<? extends BaseWagerExpect> clazz = rule.getClazz();
        BaseWagerExpect result;
        try {
            result = objectMapper.readValue(expect, clazz);
        } catch (JsonProcessingException e) {
            log.info("竞猜数据不正确");
            throw new AppClientException("竞猜数据不正确");
        }
        return result;
    }

}
