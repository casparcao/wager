package top.mikecao.wager.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import top.mikecao.wager.entities.Rule;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class WagerRequest {
    /**
     * 参与竞猜的比赛
     */
    @Min(value = 1, message = "缺少参与竞猜的比赛信息")
    private long game;
    /**
     * 竞猜规则，胜负？比分？
     */
    @NotNull(message = "缺少竞猜方式")
    private Rule rule;
    /**
     * 参与人
     */
    private long user;
    /**
     * 押注数量
     */
    @NotNull(message = "竞猜金额不能为空")
    @DecimalMin(value = "0.01", message = "竞猜金额最少为0.01元")
    private BigDecimal amount;
    /**
     * 期望结果，猜测结果，具体数据格式由rule值决定
     */
    @NotBlank(message = "竞猜结果不能为空")
    private String expect;

}
