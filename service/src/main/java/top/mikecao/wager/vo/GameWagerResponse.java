package top.mikecao.wager.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import top.mikecao.wager.entities.Rule;
import top.mikecao.wager.vo.expect.BaseWagerExpect;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class GameWagerResponse {
    /**
     * 所属用户
     */
    private String user;
    /**
     * 竞猜金额
     */
    private BigDecimal amount;
    /**
     * 竞猜方式
     */
    private Rule rule;
    /**
     * 竞猜结果
     */
    private BaseWagerExpect expect;
    /**
     * 竞猜日期
     */
    private Date date;
    /**
     * 是否猜对
     */
    private boolean right;
    /**
     *
     */
    private BigDecimal win;
}
