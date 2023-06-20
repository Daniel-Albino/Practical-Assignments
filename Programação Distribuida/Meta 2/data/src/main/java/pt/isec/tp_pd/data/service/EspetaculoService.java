package pt.isec.tp_pd.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.isec.tp_pd.data.models.Espetaculo;
import pt.isec.tp_pd.data.repository.EspetaculoRepository;

import java.util.List;

@Service
public class EspetaculoService {
    private EspetaculoRepository espetaculoRepository;

    @Autowired
    public EspetaculoService(EspetaculoRepository espetaculoRepository){
        this.espetaculoRepository = espetaculoRepository;
    }

    public List<Espetaculo> getAllEspetaculo() {
        return espetaculoRepository.findAll();
    }
}
