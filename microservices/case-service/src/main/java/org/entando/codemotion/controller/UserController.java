package org.entando.codemotion.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.entando.codemotion.entity.Process;
import org.entando.codemotion.entity.User;
import org.entando.codemotion.entity.enumeration.State;
import org.entando.codemotion.service.ProcessService;
import org.entando.codemotion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProcessService processService;

    @GetMapping("/timestamp")
    @PreAuthorize("hasAnyAuthority('case-management-admin')")
    public @ResponseBody Map<String, String> timestamp() {
        return Map.of("timestamp", new Date().toString());
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('case-management-entry')")
    public @ResponseBody List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/users")
    @PreAuthorize("hasAnyAuthority('case-management-entry')")
    public @ResponseBody User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/processes")
    @PreAuthorize("hasAnyAuthority('case-management-admin')")
    public @ResponseBody List<Process> getProcesses() {
        return processService.getAllProcesses();
    }

    @GetMapping("/process/{id}")
    @PreAuthorize("hasAnyAuthority('case-management-admin')")
    public @ResponseBody Optional<Process> getProcess(@PathVariable Long id) {
        Optional<Process> process = processService.getProcess(id);
        if (!process.isPresent()) {
            Process pro = new Process();

            pro.setId(2677L);
            pro.setPid(2381L);
            pro.setCreated(LocalDateTime.now());
            pro.setState(State.CREATED);
            pro.setNote("nota");

            Optional optional = Optional.ofNullable(pro);
            return optional;
        }
        return process;
    }

    @PostMapping("/processes")
    @PreAuthorize("hasAnyAuthority('case-management-entry')")
    public @ResponseBody Process createProcess(@RequestBody Process process) {
        return processService.crateProcess(process);
    }

}