package top.mikecao.wager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.mikecao.wager.common.Result;
import top.mikecao.wager.services.RegisterService;
import top.mikecao.wager.vo.SignUpRequest;

@RestController
public class UserController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("/api/sign/up")
    public Result<Void> register(@RequestBody @Validated SignUpRequest request){
        return registerService.action(request);
    }
}
