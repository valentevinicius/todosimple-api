package com.vinicius.todo.services;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vinicius.todo.models.User;
import com.vinicius.todo.models.enums.ProfileEnum;
import com.vinicius.todo.repositories.UserRepository;
import com.vinicius.todo.services.exceptions.DataBindingViolationException;
import com.vinicius.todo.services.exceptions.ObjectNotFoundException;

@Service
public class UserService {
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException(
            "User not found! Id: " + id + ",Type: " + User.class.getName()
        ));
    }

    @Transactional // Útil para inserções, cria conexão separada e salva dados em memória
    public User create(User obj) {
        obj.setId(null);
        obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
        obj = this.userRepository.save(obj);
        return obj;
    }

    @Transactional
    public User update(User obj) {
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());
        newObj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        return this.userRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Not possible to delete, there are related entities(Tasks)");
        }
    }

}
