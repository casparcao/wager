package top.mikecao.wager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.mikecao.wager.common.Result;
import top.mikecao.wager.config.auth.Sessions;
import top.mikecao.wager.config.auth.Who;
import top.mikecao.wager.services.WagerService;
import top.mikecao.wager.vo.GameWagerResponse;
import top.mikecao.wager.vo.WagerRequest;
import top.mikecao.wager.vo.MineWagerResponse;

import java.util.List;

@RestController
public class WagerController {

    @Autowired
    private WagerService wagerService;

    @PostMapping("/api/wager")
    public Result<Void> wager(@RequestBody @Validated WagerRequest request){
        Who who = Sessions.current();
        return wagerService.bet(who, request);
    }

    /**
     * 我的竞猜
     */
    @GetMapping("/api/wager/mine")
    public Result<List<MineWagerResponse>> mine(@RequestParam(value = "game", required = false, defaultValue = "0") long game){
        Who who = Sessions.current();
        return wagerService.mine(who.getId(), game);
    }

    /**
     * 某场游戏的所有竞猜记录
     */
    @GetMapping("/api/wager/game")
    public Result<List<GameWagerResponse>> game(@RequestParam(value = "game") long game){
        return wagerService.game(game);
    }

}
