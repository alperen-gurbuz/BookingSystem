import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Arguments {
    private VoyageController controller;
    private String content;
    private String command;
    private Map<String, CommandHandler> commandHandlers;
    private Map<String, CommandHandler> voyageHandlers;



    public Arguments(String line, VoyageController controller) {
        this.controller = controller;
        commandHandlers = new HashMap<>();
        voyageHandlers = new HashMap<>();

        commandHandlers.put("INIT_VOYAGE", this::initVoyageHandler);
        commandHandlers.put("CANCEL_VOYAGE", this::cancelVoyageHandler);
        commandHandlers.put("PRINT_VOYAGE", this::printVoyageHandler);
        commandHandlers.put("SELL_TICKET", this::sellTicketHandler);
        commandHandlers.put("REFUND_TICKET", this::refundTicketHandler);
        commandHandlers.put("Z_REPORT", this::zReportHandler);
        voyageHandlers.put("Minibus", this::minibusVoyageHandler);
        voyageHandlers.put("Standard", this::standardVoyageHandler);
        voyageHandlers.put("Premium", this::premiumVoyageHandler);


        String[] parts = line.split("\t");
        command = parts[0];
        String[] arguments = Arrays.copyOfRange(parts, 1, parts.length);
        commandHandlers.getOrDefault(command, this::defaultHandler).handle(arguments);
    }

    private void initVoyageHandler(String[] arguments) {
        String voyageType = arguments[0];
        String[] remainingArguments = Arrays.copyOfRange(arguments, 1, arguments.length);
        voyageHandlers.getOrDefault(voyageType, this::defaultHandler2).handle(remainingArguments);
    }
    private void cancelVoyageHandler(String[] arguments) {
        try {
            content = controller.CancelVoyage(Integer.valueOf(arguments[0]));
        } catch (Exception e) {
            content = "ERROR: Erroneous usage of \"CANCEL_VOYAGE\" command!";
        }
    }
    private void printVoyageHandler(String[] arguments) {
        try {
            content = controller.PrintVoyage(Integer.valueOf(arguments[0]));
        } catch (Exception e) {
            content = "ERROR: Erroneous usage of \"PRINT_VOYAGE\" command";
        }
    }
    private void sellTicketHandler(String[] arguments) {
        try {
            content = controller.SellTicket(arguments);
        } catch (Exception e) {
            content = "ERROR: Erroneous usage of \"SELL_TICKET\" command!";
        }
    }
    private void refundTicketHandler(String[] arguments) {
        try {
            content = controller.RefundTicket(arguments);
        } catch (Exception e) {
            content = "ERROR: Erroneous usage of \"REFUND_TICKET\" command!";
        }
    }
    private void zReportHandler(String[] arguments) {
        content = controller.ZReport();
    }
    private void defaultHandler(String[] arguments) {
        content = String.format("ERROR: There is no command namely %s", command);
    }
    private void minibusVoyageHandler(String[] arguments) {
        Voyage voyage = new Minibus(Arrays.asList(arguments));
        controller.InitVoyage(voyage);
        content = String.format(Locale.US, "Voyage %d was initialized as a minibus (2) voyage from %s to %s with %.2f TL priced %d regular seats. Note that minibus tickets are not refundable.",
                voyage.getID(), voyage.getFrom(), voyage.getTo(), voyage.getPrice(), voyage.getRegularSeats());
    }
    private void standardVoyageHandler(String[] arguments) {
        Voyage voyage = new Standard(Arrays.asList(arguments));
        controller.InitVoyage(voyage);
        content = String.format(Locale.US, "Voyage %d was initialized as a standard (2+2) voyage from %s to %s with %.2f TL priced %d regular seats. Note that refunds will be %d%% less than the paid amount.",
                voyage.getID(), voyage.getFrom(), voyage.getTo(), voyage.getPrice(), voyage.getRegularSeats(), voyage.getRefundCut());
    }
    private void premiumVoyageHandler(String[] arguments) {
        Voyage voyage = new Premium(Arrays.asList(arguments));
        controller.InitVoyage(voyage);
        content = String.format(Locale.US, "Voyage %d was initialized as a premium (1+2) voyage from %s to %s with %.2f TL priced %d regular seats and %.2f TL priced %d premium seats. Note that refunds will be %d%% less than the paid amount.",
                voyage.getID(), voyage.getFrom(), voyage.getTo(), voyage.getPrice(), voyage.getRegularSeats(), voyage.getPremiumFee(), voyage.getPremiumSeats(), voyage.getRefundCut());
    }
    private void defaultHandler2(String[] arguments) {
        content = "ERROR: Erroneous usage of \"INIT_VOYAGE\" command!";
    }
    private interface CommandHandler {
        void handle(String[] arguments);
    }

    @Override
    public String toString() {
        return content + "";
    }
}