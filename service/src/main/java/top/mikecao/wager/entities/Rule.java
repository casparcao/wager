package top.mikecao.wager.entities;

import top.mikecao.wager.vo.expect.*;

/**
 * 竞猜规则
 */
public enum Rule {
    /**
     * 输赢
     */
    WIN_LOSE(WinLoseWagerExpect.class),
    /**
     * 比分，精准比分
     */
    SCORE(ScoreWagerExpect.class),
    /**
     * 净胜球
     */
    SCORE_DIFF(ScoreDiffWagerExpect.class),
    /**
     * 总进球数量
     */
    TOTAL_SCORE(TotalScoreWagerExpect.class);

    private final Class<? extends BaseWagerExpect> clazz;

    private Rule(Class<? extends BaseWagerExpect> clazz){
        this.clazz = clazz;
    }

    public Class<? extends BaseWagerExpect> getClazz(){
        return this.clazz;
    }
}
