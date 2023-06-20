package pt.isec.tp_pd.data;

public class Reserve {
    private final int id;
    private String date_hour;
    private int paid;
    private int id_user;
    private int id_show;

    public Reserve(int id, String date_hour, int paid, int id_user, int id_show) {
        this.id = id;
        this.date_hour = date_hour;
        this.paid = paid;
        this.id_user = id_user;
        this.id_show = id_show;
    }

    public int getId() {
        return id;
    }

    /*public void setId(int id) {
        this.id = id;
    }*/

    public String getDate_hour() {
        return date_hour;
    }

    public void setDate_hour(String date_hour) {
        this.date_hour = date_hour;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_show() {
        return id_show;
    }

    public void setId_show(int id_show) {
        this.id_show = id_show;
    }

    @Override
    public String toString() {
        return "\nReserve {" +
            " id: " + id +
            ", date_hour: \"" + date_hour + "\"" +
            ", paid: " + paid +
            ", id_user: " + id_user +
            ", id_show: " + id_show +
        " }";
    }
}
