package pt.isec.tp_pd.data.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pt.isec.tp_pd.data.models.Reserva;
import pt.isec.tp_pd.data.models.Utilizador;
import pt.isec.tp_pd.data.service.ReservaService;
import pt.isec.tp_pd.data.service.UtilizadorService;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("utilizador")

public class UtilizadorController {

    private UtilizadorService utilizadorService;
    private ReservaService reservaService;

    @Autowired
    public UtilizadorController(UtilizadorService utilizadorService, ReservaService reservaService) {
        this.reservaService = reservaService;
        this.utilizadorService = utilizadorService;
    }

    @PostMapping
    public ResponseEntity<String> createUser(
            @RequestBody Utilizador newUser,
            @CurrentSecurityContext(expression = "authentication") Authentication authentication) {

        if (authentication.getAuthorities().toString().equals("[SCOPE_ADMIN]")) {

            List<Utilizador> aux = utilizadorService.getAllUtilizadores();

            for(Utilizador user : aux)
                if(user.getUsername().equals(newUser.getUsername()) || user.getNome().equals(newUser.getNome()))
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user already exists.");

            return ResponseEntity.status(HttpStatus.CREATED).body(utilizadorService.createUtilizador(newUser).toString());
        }else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You don't have any permission!");
    }

    @GetMapping("all")
    public ResponseEntity<String> getAllUsers(@CurrentSecurityContext(expression = "authentication") Authentication authentication) {

        if (authentication.getAuthorities().toString().equals("[SCOPE_ADMIN]"))
            return ResponseEntity.ok().body(utilizadorService.getAllUtilizadores().toString());
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You don't have any permission!");
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable("id") Integer id,
            @CurrentSecurityContext(expression="authentication") Authentication authentication) {

        if (authentication.getAuthorities().toString().equals("[SCOPE_ADMIN]")) {

            List<Utilizador> aux = utilizadorService.getAllUtilizadores();
            List<Reserva> Reserveaux = reservaService.getAllReserva();

            for(Reserva reser : Reserveaux)
                if(reser.getId_utilizador().equals(id))
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user has some reverses.");

            for (Utilizador user : aux)
                if (user.getId().equals(id) && user.getAutenticado() == 1)
                    return ResponseEntity.ok().body(utilizadorService.deleteUtilizador(id).toString());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user doesn't exists.");
        }else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You don't have any permission!");
    }
}
