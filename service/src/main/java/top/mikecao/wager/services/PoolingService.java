package top.mikecao.wager.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.mikecao.wager.common.KeyGen;
import top.mikecao.wager.entities.Game;
import top.mikecao.wager.entities.Pooling;
import top.mikecao.wager.entities.Rule;
import top.mikecao.wager.entities.Wager;
import top.mikecao.wager.exception.AppClientException;
import top.mikecao.wager.repositories.GameRepository;
import top.mikecao.wager.repositories.PoolingRepository;
import top.mikecao.wager.repositories.WagerRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 奖池服务
 */
@Service
@Slf4j
public class PoolingService {

    @Autowired
    private PoolingRepository poolingRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private WagerRepository wagerRepository;
    @Autowired
    private KeyGen key;

    /**
     * 更新奖池金额
     * @param game game
     * @param rule rule
     * @param amount amount
     */
    public void update(long game, Rule rule, BigDecimal amount) {
        Optional<Pooling> opt = poolingRepository.findByGameAndRule(game, rule);
        Pooling pooling = opt.orElse(new Pooling());
        pooling.setId(pooling.getId() > 0? pooling.getId() : key.next());
        pooling.setGame(game);
        pooling.setRule(rule);
        pooling.setAmount(amount.add(pooling.getAmount()));
        poolingRepository.save(pooling);
    }

    /**
     * 比赛结束后，奖池结算
     */
    public void clear(long gid){
        Optional<Game> optGame = gameRepository.findById(gid);
        Game game = optGame.orElseThrow(() -> new AppClientException("无该比赛"));
        if(!game.isOver()){
            //比赛还没结束
            log.info("比赛还未结束>>" + game);
            return;
        }
        if(game.isClear()){
            log.info("比赛已结清>>" + game);
            return;
        }
        game.setClear(true);
        gameRepository.save(game);
        //查询该比赛所有人的竞猜结果
        //计算每个人的最终收益
        //将奖池中的钱分配给大家
        List<Wager> wagers = wagerRepository.findByGame(gid);
        //判断谁猜测对，谁猜测错了
        wagers = hit(game, wagers);
        if(wagers.isEmpty()){
            log.info("比赛{}没有人猜对", game.getTitle());
            return;
        }
        Map<Rule, List<Wager>> map = wagers.stream()
                .collect(Collectors.groupingBy(Wager::getRule));
        List<Pooling> allPooling = poolingRepository.findByGame(gid);
        //遍历存在的奖池，分配每个奖池的金额
        for (Pooling pooling: allPooling) {
            //标记奖池完成分配
            pooling.setDone(true);
            List<Wager> wagerOfRule = map.getOrDefault(pooling.getRule(), Collections.emptyList());
            if(wagerOfRule.isEmpty()){
                //该竞猜规则，没有人猜对
                //标记该奖池为“全部未对”
                pooling.setLoss(true);
                continue;
            }
            //总奖金金额
            BigDecimal totalOfPooling = pooling.getAmount();
            int count = wagerOfRule.size();
            pooling.setWinners(count);
            BigDecimal totalOfWager = wagerOfRule.stream()
                    .map(Wager::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            wagerOfRule.forEach(w -> {
                BigDecimal win = w.getAmount()
                        .divide(totalOfWager, 4, RoundingMode.HALF_UP)
                        .multiply(totalOfPooling);
                w.setWin(win);
                w.setRight(true);
            });
        }
        wagerRepository.saveAll(wagers);
        poolingRepository.saveAll(allPooling);
    }

    private List<Wager> hit(Game game, List<Wager> wagers) {
        Game.Team home = game.getHome();
        Game.Team guest = game.getGuest();
        return wagers.stream()
                .filter(wager -> wager.getExpect().right(home.getScore(), guest.getScore()))
                .collect(Collectors.toList());
    }

}
