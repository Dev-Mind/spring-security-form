package com.devmind.demo.security.api;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/users")
public class SecurityController {

    @GetMapping(path = "/me")
    public UserDetails findUserName(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }


}
