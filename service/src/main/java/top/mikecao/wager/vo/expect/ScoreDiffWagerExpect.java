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
public class ScoreDiffWagerExpect extends BaseWagerExpect {
    /**
     * home - guest
     * 主队1球，客队2球=-1
     */
    private int diff;

    @Override
    public boolean right(int sh, int gh) {
        return sh - gh == diff;
    }

}
