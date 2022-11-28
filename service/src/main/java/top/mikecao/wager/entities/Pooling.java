package top.mikecao.wager.entities;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * 代表奖池，一种竞猜对应一种奖池
 * 每场比赛可以有多种竞猜方式，胜负，比分，净胜球，总进球数量
 */
@Data
@Accessors(chain = true)
@Document("pooling")
public class Pooling {
    private long id;
    /**
     * 奖池总金额
     */
    private BigDecimal amount = BigDecimal.ZERO;
    /**
     * 所属比赛
     */
    private long game;
    /**
     * 竞猜规则
     */
    private Rule rule;
    /**
     * 是否开奖（分配）完成
     */
    private boolean done;
    /**
     * 是否为流局（所有人没有答对）
     */
    private boolean loss;
    /**
     * 猜对人数
     */
    private int winners;

}
