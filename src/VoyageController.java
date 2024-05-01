import java.util.HashMap;
import java.util.Locale;

public class VoyageController {
    private String content;
    private HashMap<Integer, Voyage> voyages = new HashMap<>();

    public void InitVoyage (Voyage voyage) {
        voyages.put(voyage.getID(), voyage);
    }
    public String PrintVoyage (Integer ID) {
        Voyage voyage = voyages.get(ID);
        content = voyage.getLayout();
        return content;
    }
    public String ZReport () {
        content = "Z Report:\n";
        String seperator = "----------------\n";
        for (Voyage voyage : voyages.values()) {
            content += seperator;
            content += voyage.getLayout();
            content += "\n";
        }
        content += seperator;
        content = content.substring(0, (content.length()-1));
        return content;
    }
    public String CancelVoyage (Integer ID) {
        Voyage voyage = voyages.remove(ID);
        voyage.setRevenue(voyage.getRevenue()*voyage.getRefundCut());
        content = String.format("Voyage %d was successfully cancelled!\nVoyage details can be found below:\n", ID);
        content += voyage.getLayout();

        return content;
    }
    public String SellTicket (String[] arguments) {
        Voyage voyage = voyages.get(Integer.valueOf(arguments[0]));
        double totalPrice = 0.0;
        try {
            if (arguments[1].contains("_")) {
                String seatList = "";
                double i = 0;
                String[] seats = arguments[1].split("_");
                for (String seat : seats) {
                    if (voyage.getSoldSeats().contains(Integer.valueOf(seat))) {
                        return "One or more seat already sold.";
                    } else {
                        if (voyage instanceof Premium && Integer.valueOf(seat) % 3 == 1) {
                            totalPrice += voyage.getPremiumFee();
                        } else {
                            totalPrice += voyage.getPrice();
                        }
                        voyage.setSoldSeats(Integer.valueOf(seat), "sell");
                        seatList += seat + "-";
                        i += 1;
                    }
                }
                seatList = seatList.substring(0, seatList.length() - 1);
                content = String.format(Locale.US, "Seat %s of the Voyage %d from %s to %s was successfully sold for %.2f TL.",
                        seatList, voyage.getID(), voyage.getFrom(), voyage.getTo(), totalPrice);
                voyage.setRevenue(voyage.getRevenue() + totalPrice);
            } else {
                if (voyage.getSoldSeats().contains(Integer.valueOf(arguments[1]))) {
                    content = "This seat already sold.";
                } else {
                    if (voyage instanceof Premium && Integer.valueOf(arguments[1]) % 3 == 1) {
                        totalPrice = voyage.getPremiumFee();
                    } else {
                        totalPrice = voyage.getPrice();
                    }
                    voyage.setSoldSeats(Integer.valueOf(arguments[1]), "sell");
                    voyage.setRevenue(voyage.getRevenue() + totalPrice);
                    content = String.format(Locale.US, "Seat %s of the Voyage %d from %s to %s was successfully sold for %.2f TL.",
                            arguments[1], voyage.getID(), voyage.getFrom(), voyage.getTo(), totalPrice);
                }
            }
        } catch (Exception e) {
            return "Erroneous usage of \"SELL_TICKET\" command!";
        }
        voyage.setLayout();
        return content;
    }
    public String RefundTicket(String[] arguments) {
        Voyage voyage = voyages.get(Integer.valueOf(arguments[0]));
        double totalRefundPrice;
        double totalPrice = 0.0;
        if (!(voyage instanceof Minibus)) {
            double refundPrice = (100 - voyage.getRefundCut()) * voyage.getPrice()/100;
            if (arguments[1].contains("_")) {
                String seatList = "";
                double i = 0;
                String[] seats = arguments[1].split("_");
                for (String seat : seats) {
                    if (voyage.getSoldSeats().contains(Integer.valueOf(seat))) {
                        voyage.setSoldSeats(Integer.valueOf(seat), "refund");
                        seatList += seat + "-";
                        if (voyage instanceof Premium && Integer.valueOf(seat) % 3 == 1) {
                            totalPrice += voyage.getPremiumFee();
                        } else {
                            totalPrice += voyage.getPrice();
                        }
                        i += 1;

                    } else {
                        return "There is one or more not refundable ticket.";
                    }
                }
                totalRefundPrice = (100 - voyage.getRefundCut())*totalPrice/100;
                seatList = seatList.substring(0, seatList.length() - 1);
                content = String.format(Locale.US, "Seat %s of the Voyage %d from %s to %s was successfully refunded for %.2f TL.",
                        seatList, voyage.getID(), voyage.getFrom(), voyage.getTo(), totalRefundPrice);
                voyage.setRevenue(voyage.getRevenue() - totalRefundPrice);
            } else {
                if (voyage.getSoldSeats().contains(Integer.valueOf(arguments[1]))) {
                    voyage.setSoldSeats(Integer.valueOf(arguments[1]), "refund");
                    refundPrice = (100 - voyage.getRefundCut()) * voyage.getPrice()/100;
                    voyage.setRevenue(voyage.getRevenue() - refundPrice);
                    content = String.format(Locale.US, "Seat %s of the Voyage %d from %s to %s was successfully refunded for %.2f TL.",
                            arguments[1], voyage.getID(), voyage.getFrom(), voyage.getTo(), refundPrice);
                } else {
                    content = "There is one or more not refundable ticket.";
                }
            }
            voyage.setLayout();
        } else { content = "ERROR: Minibus tickets are not refundable!";}
        return content;
    }
}
