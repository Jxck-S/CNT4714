import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionLogger {

    private static final String FILE_PATH = "transactions.csv";

    public static void logTransaction(ZonedDateTime dateTime, String opperation, String agent, String action, double amount, int transactionNumber) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true));
            String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss z"));
            String result = String.format("%s Agent %s issued %s of $%.2f at: %s\tTransaction Number: %d\n", opperation, agent, action, amount, formattedDateTime, transactionNumber);
            writer.write(result);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // public static void main(String[] args) {
    //     ZonedDateTime dateTime = ZonedDateTime.now();
    //     logTransaction(dateTime, "\t\tWithdrawal", "W4", "withdrawal", 78.00, 40);
    //     logTransaction(dateTime, "Deposit", "D2", "deposit", 728.00, 30);

    // }
}
