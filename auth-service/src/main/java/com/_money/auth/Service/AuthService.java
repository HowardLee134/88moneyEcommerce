package com._money.auth.Service;

import com._money.auth.Dto.UserDto;

public interface AuthService {
    void register(UserDto userDto);

    String login(String email, String password);

    void validateToken(String token);
}
