package top.mikecao.wager.vo.expect;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 竞猜比分
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ScoreWagerExpect extends BaseWagerExpect {
    private int home;
    private int guest;

    @Override
    public boolean right(int sh, int gh) {
        return sh == home && gh == guest;
    }
}
