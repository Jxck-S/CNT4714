import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
public class AddTransaction {
    // Testing
    public static void main(String[] args) {
        ZonedDateTime now = ZonedDateTime.now();
        Item item =  Inventory.findItem("14");
        ItemInCart itemc = new ItemInCart(item, 4, 20, 5000);
        addTransaction(itemc, now);
    }

    public static void addTransaction(ItemInCart itemc, ZonedDateTime transaction_datetime) {
        try {
            String filePath = "transactions.csv";
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter csvWriter = new FileWriter(filePath, true);
            DateTimeFormatter id_datetime_formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
            DateTimeFormatter day_month_formatter = DateTimeFormatter.ofPattern("MMMM d");
            DateTimeFormatter time_formatter = DateTimeFormatter.ofPattern("h:mm:ss a z");
            String date_string_id = transaction_datetime.format(id_datetime_formatter);
            csvWriter.append(String.join(", ",
                date_string_id,
                itemc.item.itemId,
                itemc.item.name,
                String.valueOf(itemc.item.price),
                String.valueOf(itemc.quantity),
                String.format("%.1f", (float)itemc.discount_rate),
                String.format("$%.2f", itemc.subtotal),
                transaction_datetime.format(day_month_formatter),
                String.valueOf(transaction_datetime.getYear()),
                transaction_datetime.format(time_formatter)));
            csvWriter.append("\n");
            csvWriter.flush();
            csvWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
