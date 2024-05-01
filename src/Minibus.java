import java.util.List;

public class Minibus extends Voyage {
    public Minibus(List<String> line) {
        super(line);
        column = 2;
        super.setLayout();
        regularSeats = super.getRow()*2;
    }
}