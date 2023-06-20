package pt.isec.tp_pd.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.isec.tp_pd.data.models.Reserva;
import pt.isec.tp_pd.data.repository.ReservaRespository;

import javax.swing.*;
import java.util.List;

@Service
public class ReservaService {
    private ReservaRespository reservaRespository;

    @Autowired
    public ReservaService(ReservaRespository reservaRespository){
        this.reservaRespository = reservaRespository;
    }

    public List<Reserva> getAllReserva() {
        return reservaRespository.findAll();
    }

    public List<Reserva> getReserveByUserIdPaid(Integer id_utilizador) {
        return reservaRespository.findByPaidWhere(id_utilizador);
    }

    public List<Reserva> getReserveByUserIdUnpaid(Integer id_utilizador) {
        return reservaRespository.findByUnpaidWhere(id_utilizador);
    }
}
