package pt.isec.tp_pd.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.isec.tp_pd.data.models.Espetaculo;

@Repository
public interface EspetaculoRepository extends JpaRepository<Espetaculo, Integer> {
}
