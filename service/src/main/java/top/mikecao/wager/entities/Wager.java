package top.mikecao.wager.entities;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import top.mikecao.wager.vo.expect.BaseWagerExpect;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 竞猜，一次竞猜
 */
@Data
@Accessors(chain = true)
@Document("wager")
public class Wager {
    private long id;
    /**
     * 参与人
     */
    private long user;
    /**
     * 参与的哪场比赛
     */
    private long game;
    /**
     * 参与规则
     */
    private Rule rule;
    /**
     * 参与时间
     */
    private Date date;
    /**
     * 竞猜金额
     */
    private BigDecimal amount;
    /**
     * 期望结果，胜负，3:0，12球，3球
     * 胜负，{"winner": "home"}
     * 比分，{"home": 3, "guest": 2}
     * 净胜球数 {"winner": "home", "diff": 5}
     * 总进球数 {"total": 10}
     */
    private BaseWagerExpect expect;
    /**
     * 是否猜对
     */
    private boolean right;
    /**
     * 最终获得的奖金数量
     */
    private BigDecimal win;

}
