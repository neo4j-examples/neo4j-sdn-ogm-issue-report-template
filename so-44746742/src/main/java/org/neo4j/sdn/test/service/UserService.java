package org.neo4j.sdn.test.service;

import org.neo4j.sdn.test.domain.User;
import org.neo4j.sdn.test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void incrementCounter(Long id) {
        User user = userRepository.findOne(id);

        user.increment();
        userRepository.save(user);
    }

    @Transactional
    public void incrementCounterWithLock(Long id) {

        User user = userRepository.acquireLock(id);

        user.increment();
        userRepository.save(user);

        userRepository.releaseLock(user.getId());
    }
}
