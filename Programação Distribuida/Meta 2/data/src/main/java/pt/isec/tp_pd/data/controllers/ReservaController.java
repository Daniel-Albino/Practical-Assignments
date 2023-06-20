package pt.isec.tp_pd.data.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import pt.isec.tp_pd.data.models.Reserva;
import pt.isec.tp_pd.data.models.Utilizador;
import pt.isec.tp_pd.data.service.ReservaService;
import pt.isec.tp_pd.data.service.UtilizadorService;

import java.util.List;

@RestController
@RequestMapping("reserva")
public class ReservaController {

    private ReservaService reservaService;
    private UtilizadorService utilizadorService;

    @Autowired
    public ReservaController(ReservaService reservaService, UtilizadorService utilizadorService) {
        this.utilizadorService = utilizadorService;
        this.reservaService = reservaService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<Reserva>> getAllReserva() {
        return ResponseEntity.ok().body(reservaService.getAllReserva());
    }

    @GetMapping("/alluser")
    public ResponseEntity<List<Reserva>> getAllByUserReserva(@RequestParam(name = "PAID", required = false, defaultValue = "") String PAID, @CurrentSecurityContext(expression="authentication?.name") String username) {
        Utilizador utilizadorFounded = utilizadorService.getUtilizadorByName(username);

        //System.out.println("DEBUG: " + PAID);

        switch (PAID.toUpperCase()) {
            case "0" -> {
                return ResponseEntity.ok().body(reservaService.getReserveByUserIdUnpaid(utilizadorFounded.getId()));
            }
            case "1" -> {
                return ResponseEntity.ok().body(reservaService.getReserveByUserIdPaid(utilizadorFounded.getId()));
            }
            default -> {
                return ResponseEntity.ok().body(reservaService.getAllReserva());
            }
        }
    }
}
