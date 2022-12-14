package com.thiendz.example.springsocket.services;

import com.thiendz.example.springsocket.auths.JwtTokenProvider;
import com.thiendz.example.springsocket.dto.api.req.LoginReq;
import com.thiendz.example.springsocket.dto.api.req.RegisterReq;
import com.thiendz.example.springsocket.dto.api.res.LoginRes;
import com.thiendz.example.springsocket.dto.api.res.RegisterRes;
import com.thiendz.example.springsocket.model.UserProfile;
import com.thiendz.example.springsocket.model.UserWallet;
import com.thiendz.example.springsocket.repo.custom.UserProfileCustomRepository;
import com.thiendz.example.springsocket.repo.jpa.UserProfileRepository;
import com.thiendz.example.springsocket.repo.jpa.UserWalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    UserProfileRepository userProfileRepository;
    @Autowired
    UserWalletRepository userWalletRepository;

    @Autowired
    UserProfileCustomRepository userProfileCustomRepository;

    public LoginRes login(LoginReq loginReq) {
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findFirstByUsername(loginReq.getUsername());
        if (!optionalUserProfile.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tài khoản không tồn tại");
        if (!optionalUserProfile.get().getPassword().equals(loginReq.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu không đúng");
        String token = jwtTokenProvider.generateToken(optionalUserProfile.get());
        return new LoginRes(token, userProfileCustomRepository.getUserProfileInfo(loginReq.getUsername()));
    }

    @Transactional
    public RegisterRes register(RegisterReq registerReq) {
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findFirstByUsername(registerReq.getUsername());
        if (optionalUserProfile.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tài khoản đã tồn tại");
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername(registerReq.getUsername());
        userProfile.setPassword(registerReq.getPassword());
        userProfile.setRole("USER");
        userProfile.setStatus(true);
        userProfile.setCreateAt(new Date().getTime());
        UserProfile userProfileCreated = userProfileRepository.save(userProfile);
        UserWallet userWallet = new UserWallet();
        userWallet.setUserId(userProfileCreated.getId());
        userWallet.setGold(0);
        userWallet.setSliver(0);
        userWalletRepository.save(userWallet);
        return new RegisterRes(userProfile);
    }
}
