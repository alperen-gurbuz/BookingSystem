import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Voyage {
    private Integer ID;
    private String from;
    private String to;
    private Integer row;
    protected Integer column;
    protected Integer regularSeats;
    protected Integer premiumSeats;
    private double price;
    protected Integer refundCut;
    protected double premiumFee;
    private char[][] seats;
    private List<Integer> soldSeats;
    private double revenue;
    public Voyage(List<String> line) {
        ID = Integer.valueOf(line.get(0));
        from = line.get(1);
        to = line.get(2);
        row = Integer.valueOf(line.get(3));
        price = Double.parseDouble(line.get(4));
        this.soldSeats = new ArrayList<>();
    }
    public void setLayout() {
        Integer seatNumber = 0;
        seats = new char[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                seatNumber += 1;
                if (soldSeats.contains(seatNumber)) {
                    seats[i][j] = 'X';
                } else {
                    seats[i][j] = '*';
                }
            }
        }
    }
    public Integer getID() {
        return ID;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Integer getRow() {
        return row;
    }

    public double getPrice() {
        return price;
    }
    public Integer getRegularSeats() {
        return regularSeats;
    }

    public Integer getPremiumSeats() {
        return premiumSeats;
    }

    public Integer getRefundCut() {
        return refundCut;
    }
    public double getPremiumFee() {
        return premiumFee;
    }
    public double getRevenue() {
        return revenue;
    }
    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
    public String getLayout() {

        String lines = "Voyage " + ID + "\n";
        lines += from+"-"+ to + "\n";

        for (int i = 0; i < row; i++) {
            Integer corridor = 0;
            for (int j = 0; j < column; j++) {
                corridor += 1;
                switch (column) {
                    case 2:
                        break;
                    case 3:
                        if (corridor == 2) {
                            lines += "| ";
                    }
                        break;
                    case 4:
                        if (corridor == 3) {
                            lines += "| ";
                        }
                        break;
                }

                lines += seats[i][j];
                if (j != column - 1) { lines += " ";}
            }
            lines += "\n";
        }
        lines += "Revenue: " + String.format(Locale.US,"%.2f", revenue);
        return lines;
    }
    public List<Integer> getSoldSeats() {
        return soldSeats;
    }

    public void setSoldSeats(Integer seat, String command) {
        if (command == "sell") {
            soldSeats.add(seat);
        } else {
            soldSeats.remove(seat);
        }
    }
}