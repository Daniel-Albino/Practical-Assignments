package pt.isec.tp_pd.data.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String data_hora;
    
    private Integer pago;
    
    private Integer id_utilizador;

    private Integer id_espetaculo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getData_hora() {
        return data_hora;
    }

    public void setData_hora(String data_hora) {
        this.data_hora = data_hora;
    }

    public Integer getPago() {
        return pago;
    }

    public void setPago(Integer pago) {
        this.pago = pago;
    }

    public Integer getId_utilizador() {
        return id_utilizador;
    }

    public void setId_utilizador(Integer id_utilizador) {
        this.id_utilizador = id_utilizador;
    }

    public Integer getId_espetaculo() {
        return id_espetaculo;
    }

    public void setId_espetaculo(Integer id_espetaculo) {
        this.id_espetaculo = id_espetaculo;
    }
}
