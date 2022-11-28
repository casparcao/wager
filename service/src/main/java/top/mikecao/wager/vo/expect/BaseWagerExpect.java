package top.mikecao.wager.vo.expect;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public abstract class BaseWagerExpect {

    /**
     * 判断本次猜测是否正确
     * @param sh 主队比分
     * @param gh 客队比分
     * @return 是否正确
     */
    public abstract boolean right(int sh, int gh);

}
