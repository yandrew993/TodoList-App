package com.example.demo.controller;

import com.example.demo.model.ToDo;
import com.example.demo.repository.ToDoRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ToDoController {
    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/todos")
    public String viewTodos(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = userRepository.findByUsername(userDetails.getUsername()).getId();
        model.addAttribute("todos", toDoRepository.findByUserId(userId));
        return "todos";
    }

    @PostMapping("/todos/add")
    public String addToDo(@RequestParam String description, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = userRepository.findByUsername(userDetails.getUsername()).getId();
        ToDo toDo = new ToDo();
        toDo.setDescription(description);
        toDo.setDone(false);
        toDo.setUser(userRepository.findById(userId).orElse(null));
        toDoRepository.save(toDo);
        return "redirect:/todos";
    }

    @
    @PostMapping("/todos/update")
    public String updateToDo(@RequestParam Long id, @RequestParam boolean done) {
        ToDo toDo = toDoRepository.findById(id).orElse(null);
        if (toDo != null) {
            toDo.setDone(done);
            toDoRepository.save(toDo);
        }
        return "redirect:/todos";
    }

    @PostMapping("/todos/delete")
    public String deleteToDo(@RequestParam Long id) {
        toDoRepository.deleteById(id);
        return "redirect:/todos";
    }
    
    @Controller
    public class ToDoController {
        // existing methods ...

        @GetMapping("/register")
        public String showRegistrationForm() {
            return "register";
        }

        @PostMapping("/register")
        public String registerUser(@RequestParam String username, @RequestParam String password) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(new BCryptPasswordEncoder().encode(password));
            user.setEnabled(true);
            userRepository.save(user);
            return "redirect:/login";
        }
    }

}
