package com.example.mutsamarket.user.service;

import com.example.mutsamarket.exceptions.status400.ExistentUserException;
import com.example.mutsamarket.exceptions.status404.NotFoundUserException;
import com.example.mutsamarket.user.UserRepository;
import com.example.mutsamarket.user.entity.CustomUserDetails;
import com.example.mutsamarket.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JpaUserDetailsManager implements UserDetailsManager {
    private final UserRepository userRepository;

    @Override
    public void createUser(UserDetails user) {
        if (this.userExists(user.getUsername())) throw new ExistentUserException();

        userRepository.save(((CustomUserDetails) user).getInstance());
    }

    @Override
    public void updateUser(UserDetails user) {
        UserEntity userEntity = userRepository.findByUsername(user.getUsername()).orElseThrow(NotFoundUserException::new);
        userEntity.updateInfo((CustomUserDetails) user);
        userRepository.save(userEntity);
    }

    @Override
    public void deleteUser(String username) {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(NotFoundUserException::new);
        userRepository.delete(userEntity);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        UserEntity userEntity = userRepository.findByPassword(oldPassword).orElseThrow(NotFoundUserException::new);
        userEntity.setPassword(newPassword);
        userRepository.save(userEntity);
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(NotFoundUserException::new);

        return CustomUserDetails.fromEntity(userEntity);
    }
}
