
package com.devmind.demo.security.api;

import com.devmind.demo.security.dao.UserDao;
import com.devmind.demo.security.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
//@RequestMapping("/login")
public class LoginController {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public LoginController(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<User> login(@RequestBody User user) {
        User existingUser = userDao.findByUsername(user.getUsername());
        if (existingUser == null) {
            return ResponseEntity.status(401).build();
        }
        if (!passwordEncoder.matches(existingUser.getPassword(), passwordEncoder.encode(user.getPassword()))) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(existingUser);
    }
}
