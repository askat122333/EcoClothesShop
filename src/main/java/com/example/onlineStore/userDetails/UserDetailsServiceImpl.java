package com.example.onlineStore.userDetails;


import com.example.onlineStore.entity.User;
import com.example.onlineStore.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {



    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username);
//        return new org.springframework.security.core.userdetails.User(user.getLogin(),user.getPassword(),user.getRole().getAuthorities());
        return new org.springframework.security.core.userdetails.User(user.getLogin(),user.getPassword(),user.getRole().getAuthorities());
    }
}
