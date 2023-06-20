package pt.isec.tp_pd.data;

public class ReserveSeat {
    private int id_reserve;
    private int id_seat;

    public ReserveSeat() {}

    public ReserveSeat(int id_reserve, int id_seat) {
        this.id_reserve = id_reserve;
        this.id_seat = id_seat;
    }

    public int getId_reserve() {
        return id_reserve;
    }

    public int getId_seat() {
        return id_seat;
    }

    @Override
    public String toString() {
        return "\nReservaLugar {" +
            " id_reserve: " + id_reserve +
            ", id_seat: " + id_seat +
        " }";
    }
}
