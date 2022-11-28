package top.mikecao.wager.vo.expect;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 竞猜输赢
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class WinLoseWagerExpect extends BaseWagerExpect {
    /**
     * 主队=1
     * 客队=-1
     * 大于零表示主队应，小于零表示客队赢，等于零表示平局
     */
    private byte winner = 0;

    @Override
    public boolean right(int sh, int gh) {
        return (sh - gh > 0 && winner > 0)
                || (sh - gh == 0 && winner == 0)
                || (sh - gh < 0 && winner < 0);
    }

}
