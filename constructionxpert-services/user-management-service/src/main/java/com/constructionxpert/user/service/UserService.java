package com.constructionxpert.user.service;


import com.constructionxpert.user.model.Users;
import com.constructionxpert.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository utilisateurRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users saveUtilisateur(Users utilisateur) {
        String password = utilisateur.getPassword();
        if (password != null) {
            String hashedPassword = passwordEncoder.encode(password);
            utilisateur.setPassword(hashedPassword);
        }
        return utilisateurRepository.save(utilisateur);
    }


    public List<Users> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public Users getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id).orElseThrow();
    }

    public Users updateUtilisateur(Long id, Users utilisateurDetails) {
        Users utilisateur = getUtilisateurById(id);
        utilisateur.setUsername(utilisateurDetails.getUsername());
        utilisateur.setEmail(utilisateurDetails.getEmail());
        return utilisateurRepository.save(utilisateur);
    }



}