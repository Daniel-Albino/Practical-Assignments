package pt.isec.tp_pd.data.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.isec.tp_pd.data.models.Utilizador;
import pt.isec.tp_pd.data.repository.UtilizadorRepository;
import java.util.List;

@Service
public class UtilizadorService {

    private UtilizadorRepository utilizadorRepository;

    @Autowired
    public UtilizadorService(UtilizadorRepository utilizadorService)
    {
        this.utilizadorRepository = utilizadorService;
    }


    public Utilizador createUtilizador(Utilizador c) {
        c.setId(null);
        return utilizadorRepository.save(c);
    }

    public List<Utilizador> getAllUtilizadores() {
        return utilizadorRepository.findAll();
    }

    public Utilizador getUtilizadorByName(String username) {
        return utilizadorRepository.findByUsernameWhere(username);
    }

    public Utilizador deleteUtilizador(Integer id) {
        Utilizador curContact = utilizadorRepository.findById(id).get();
        utilizadorRepository.deleteById(id);
        return curContact;
    }


}
