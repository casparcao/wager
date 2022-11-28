package top.mikecao.wager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.mikecao.wager.common.KeyGen;
import top.mikecao.wager.common.Result;
import top.mikecao.wager.config.auth.Who;
import top.mikecao.wager.entities.Game;
import top.mikecao.wager.entities.User;
import top.mikecao.wager.entities.Wager;
import top.mikecao.wager.exception.AppClientException;
import top.mikecao.wager.repositories.GameRepository;
import top.mikecao.wager.repositories.UserRepository;
import top.mikecao.wager.repositories.WagerRepository;
import top.mikecao.wager.validator.WagerExpectValidator;
import top.mikecao.wager.vo.GameWagerResponse;
import top.mikecao.wager.vo.WagerRequest;
import top.mikecao.wager.vo.MineWagerResponse;
import top.mikecao.wager.vo.expect.BaseWagerExpect;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class WagerService {

    @Autowired
    private KeyGen key;
    @Autowired
    private WagerRepository wagerRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private WagerExpectValidator wagerExpectValidator;
    @Autowired
    private PoolingService poolingService;
    @Autowired
    private UserRepository userRepository;

    /**
     * 参与一次竞猜
     */
    public Result<Void> bet(Who who, WagerRequest request) {
        //校验输入数据是否正确
        BaseWagerExpect expect = wagerExpectValidator.validate(request);
        //比赛开始后，停止竞猜
        Optional<Game> optGame = gameRepository.findById(request.getGame());
        if(optGame.isEmpty()){
            throw new AppClientException("无该比赛");
        }
        Game game = optGame.get();
        if(game.isStarted()){
            throw new AppClientException("比赛已开始，无法参与");
        }
        Wager wager = new Wager();
        wager.setId(key.next());
        wager.setGame(request.getGame());
        wager.setDate(new Date());
        wager.setRule(request.getRule());
        wager.setExpect(expect);
        wager.setAmount(request.getAmount());
        wager.setUser(who.getId());
        wagerRepository.save(wager);
        poolingService.update(request.getGame(),request.getRule(), request.getAmount());
        return Result.ok();
    }

    public Result<List<MineWagerResponse>> mine(long uid, long game) {
        List<Wager> entities ;
        if(game > 0){
            entities = wagerRepository.findByUserAndGame(uid, game);
        }else{
            entities = wagerRepository.findByUser(uid);
        }
        if(entities.isEmpty()){
            return Result.ok();
        }
        List<Long> gids = entities.stream()
                .map(Wager::getGame)
                .collect(Collectors.toList());
        Iterable<Game> games = gameRepository.findAllById(gids);
        Map<Long, Game> gmap = StreamSupport.stream(games.spliterator(), false)
                .collect(Collectors.toMap(Game::getId, x -> x));
        List<MineWagerResponse> result = new ArrayList<>();
        for (Wager wager: entities) {
            MineWagerResponse wr = new MineWagerResponse();
            wr.setAmount(wager.getAmount());
            wr.setRule(wager.getRule());
            wr.setDate(wager.getDate());
            wr.setExpect(wager.getExpect());
            wr.setGame(gmap.get(wager.getGame()));
            wr.setRight(wager.isRight());
            wr.setWin(wager.getWin());
            result.add(wr);
        }
        return Result.ok(result);
    }

    public Result<List<GameWagerResponse>> game(long game) {
        List<Wager> entities = wagerRepository.findByGame(game);
        if(entities.isEmpty()){
            return Result.ok();
        }
        List<Long> uids = entities.stream()
                .map(Wager::getUser)
                .collect(Collectors.toList());
        Iterable<User> users = userRepository.findAllById(uids);
        Map<Long, User> umap = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toMap(User::getId, x -> x));
        List<GameWagerResponse> result = new ArrayList<>();
        for (Wager wager: entities) {
            GameWagerResponse wr = new GameWagerResponse();
            wr.setAmount(wager.getAmount());
            wr.setRule(wager.getRule());
            wr.setDate(wager.getDate());
            wr.setExpect(wager.getExpect());
            wr.setRight(wager.isRight());
            wr.setWin(wager.getWin());
            wr.setUser(umap.getOrDefault(wager.getUser(), new User()).getUsername());
            result.add(wr);
        }
        return Result.ok(result);
    }
}
