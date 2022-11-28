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
public class TotalScoreWagerExpect extends BaseWagerExpect {
    private int total;

    @Override
    public boolean right(int sh, int gh) {
        return sh + gh == total;
    }
}
