package org.neo4j.boot.test.web;

import org.neo4j.boot.test.domain.User;
import org.neo4j.boot.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/test", method = RequestMethod.POST, consumes = "application/json")
    public User create(@RequestBody User user) {
        return userService.createUser(user);
    }

}
