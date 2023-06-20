package pt.isec.tp_pd.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.isec.tp_pd.data.models.Utilizador;

import java.util.List;

@Repository
public interface UtilizadorRepository extends JpaRepository<Utilizador, Integer> {

    @Query("select u from Utilizador u where u.username = :#{#username}")
    Utilizador findByUsernameWhere(@Param("username") String username);

    @Query("select u from Utilizador u where u.username = :#{#username} AND u.password = :#{#password}")
    List<Utilizador> findByUsernameWhere(@Param("username") String username, @Param("password") String password);
}


