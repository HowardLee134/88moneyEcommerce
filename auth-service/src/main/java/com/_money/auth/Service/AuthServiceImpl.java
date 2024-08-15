package com._money.auth.Service;

import com._money.auth.Config.JwtUtil;
import com._money.auth.Dto.UserDto;
import com._money.auth.Model.User;
import com._money.auth.Repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Hardcoded secret key for JWT
    private final String secretKey = "MySecretKey";

    @Override
    public void register(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setUsername(userDto.getUsername());
        user.setPhone(userDto.getPhone());
        userRepository.save(user);
    }

    @Override
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Generate JWT token with JwtUtil
        return JwtUtil.generateToken(user.getEmail());
    }

    @Override
    public void validateToken(String token) {
        try {
            String secretKey = "MySecretKey"; // Ensure this matches the key used to sign the token

            Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token.replace("Bearer ", ""));

            // If the token is valid, the method will complete without exception
        } catch (Exception e) {
            throw new RuntimeException("Invalid credentials", e);
        }
    }
}
