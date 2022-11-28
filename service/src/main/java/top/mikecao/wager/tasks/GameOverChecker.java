package top.mikecao.wager.tasks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.mikecao.wager.entities.Game;
import top.mikecao.wager.services.GameService;
import top.mikecao.wager.services.PoolingService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GameOverChecker {

    @Autowired
    private PoolingService poolingService;
    @Autowired
    private GameService gameService;

    /**
     * 每小时执行一次，半点执行
     */
    @Scheduled(cron = "0 30 * * * *")
    public void check(){
        //查询出今天开始的比赛，判断是否结束
        List<Game> games = gameService.today();
        log.info("定时任务执行，当天比赛有{}场次", games.size());
        games = games.stream()
                .filter(Game::isStarted)
                .filter(Game::isOver)
                .collect(Collectors.toList());
        //如果结束，执行结算
        games.forEach(game -> poolingService.clear(game.getId()));
    }

}
