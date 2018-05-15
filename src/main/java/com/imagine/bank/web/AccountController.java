package com.imagine.bank.web;
import com.imagine.bank.service.AccountService;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping(""/api/accounts"")
public class AccountController {
    private final AccountService service;
    public AccountController(AccountService service) { this.service = service; }
    
    @GetMapping(""/{id}/balance"")
    public Object getBalance(@PathVariable String id) {
        return service.getBalance(id);
    }
}
