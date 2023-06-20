package pt.isec.tp_pd.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.isec.tp_pd.data.models.Reserva;

import java.util.List;

@Repository
public interface ReservaRespository extends JpaRepository<Reserva, Integer> {

    @Query("select r from Reserva r where r.id_utilizador = :#{#id_utilizador} AND r.pago = 1")
    List<Reserva> findByPaidWhere(@Param("id_utilizador") Integer id_utilizador);

    @Query("select r from Reserva r where r.id_utilizador = :#{#id_utilizador} AND r.pago = 0")
    List<Reserva> findByUnpaidWhere(@Param("id_utilizador") Integer id_utilizador);
}
