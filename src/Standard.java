import java.util.List;

public class Standard extends Voyage {
    public Standard(List<String> line) {
        super(line);
        column = 4;
        super.setLayout();
        refundCut = Integer.valueOf(line.get(5));
        regularSeats = super.getRow()*4;
    }
}