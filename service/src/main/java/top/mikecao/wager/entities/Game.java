package top.mikecao.wager.entities;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

/**
 * 一场球赛
 */
@Data
@Accessors(chain = true)
@Document("game")
public class Game {

    private long id;
    /**
     * 比赛日期
     */
    private Date date;

    /**
     * A组第一轮
     */
    private String title;
    /**
     * 主队信息
     */
    private Team home;
    /**
     * 客队信息
     */
    private Team guest;
    /**
     * 是否已结算
     */
    private boolean clear;
    /**
     * 是否结束
     */
    public boolean isOver(){
        if (Objects.isNull(date)) {
            return true;
        }
        //假设两个小时内一定完成
        Instant endTime = date.toInstant().plusSeconds(120L * 60);
        return endTime.isBefore(Instant.now());
    }

    public boolean isStarted(){
        return Objects.isNull(date) || new Date().after(date);
    }

    @Data
    @Accessors(chain = true)
    public static class Team {
        private String name;
        private String icon;
        private int score;
    }
}
