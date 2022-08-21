package com.thiendz.example.springsocket.controllers;

import com.thiendz.example.springsocket.dto.api.req.LoginReq;
import com.thiendz.example.springsocket.dto.api.req.RegisterReq;
import com.thiendz.example.springsocket.dto.api.res.LoginRes;
import com.thiendz.example.springsocket.dto.api.res.RegisterRes;
import com.thiendz.example.springsocket.dto.api.res.Response;
import com.thiendz.example.springsocket.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    ResponseEntity<Response<LoginRes>> login(@RequestBody LoginReq loginReq) {
        return ResponseEntity.ok(Response.success(1, "Đăng nhập thành công", authService.login(loginReq)));
    }

    @PostMapping("/register")
    ResponseEntity<Response<RegisterRes>> register(@RequestBody RegisterReq registerReq) {
        return ResponseEntity.ok(Response.success(1, "Đăng kí thành công", authService.register(registerReq)));
    }
}
