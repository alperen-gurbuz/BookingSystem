import java.util.List;

public class Premium extends Voyage {
    private double premiumratio;
    public Premium(List<String> line) {
        super(line);
        column = 3;
        super.setLayout();
        regularSeats = super.getRow()*2;
        premiumSeats = super.getRow();
        refundCut = Integer.valueOf(line.get(5));
        premiumratio = Integer.valueOf(line.get(6));
        premiumFee = (1 + premiumratio/100)*getPrice();
    }
}