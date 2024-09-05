package com.constructionxpert.user.controller;

import com.constructionxpert.user.model.Users;
import com.constructionxpert.user.security.JwtAuth;
import com.constructionxpert.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Users")
public class UserController {

    @Autowired
    UserService utilisateurService;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public Users saveUtilisateur(@RequestBody Users utilisateur) {
        return utilisateurService.saveUtilisateur(utilisateur);
    }
    @GetMapping("/showAllUtilisateur")
    public List<Users> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody Utillisateur utilisateur) {
//        System.out.println("///////////////////"+utilisateur.getPassword()+"//////////////"+utilisateur.getUsername());
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(utilisateur.getUsername(), utilisateur.getPassword())
//        );
//        String token = JwtAuth.generateToken(utilisateur.getUsername());
//        return ResponseEntity.ok(token);
//
//    }
@PostMapping("/login")
public ResponseEntity<?> login(@Validated @RequestBody Users utilisateur) {
    try {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(utilisateur.getUsername(), utilisateur.getPassword()));

        if (authentication.isAuthenticated()) {
            String token = JwtAuth.generateToken(utilisateur.getUsername());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login credentials");
        }
    } catch (UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found: " + e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error authenticating user: " + e.getMessage());
    }
}
}
