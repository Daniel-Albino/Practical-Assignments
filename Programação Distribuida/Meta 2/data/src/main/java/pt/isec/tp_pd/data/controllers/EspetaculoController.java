package pt.isec.tp_pd.data.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.isec.tp_pd.data.models.Espetaculo;
import pt.isec.tp_pd.data.service.EspetaculoService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("espetaculo")

public class EspetaculoController {

    private EspetaculoService espetaculoService;

    @Autowired
    public EspetaculoController(EspetaculoService espetaculoService)
    {
        this.espetaculoService = espetaculoService;
    }


    @GetMapping("all")
    public ResponseEntity<List<Espetaculo>> getAllEspetaculo(
            @RequestParam(name = "datainicio", required = false, defaultValue = "")String datainicio,
            @RequestParam(name = "datafim", required = false, defaultValue = "")String datafim)
    {
        List<Espetaculo> espetaculos = espetaculoService.getAllEspetaculo();

        if(datafim.equals("") && datainicio.equals(""))
            return ResponseEntity.ok().body(espetaculos);
        else {
            List<Espetaculo> aux = new ArrayList<>();
            for(Espetaculo espetaculo : espetaculos) {
                System.out.println("Espetaculo da basedados:"+espetaculo.getData_hora()+
                        "   datainicio:"+datainicio+"    datafim"+datafim);
                System.out.println("compare com datafim"+espetaculo.getData_hora().compareTo(datafim));
                System.out.println("compare com dataincio"+espetaculo.getData_hora().compareTo(datainicio));
                if (espetaculo.getData_hora().compareTo(datafim) < 0
                        && espetaculo.getData_hora().compareTo(datainicio) > 0) {
                    System.out.println("entrei");
                    aux.add(espetaculo);
                }
            }
            return ResponseEntity.ok().body(aux);
        }
    }
}

