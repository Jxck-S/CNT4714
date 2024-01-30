import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class Inventory {
    public static Item findItem(String itemId) {
        String csvFile = "inventory.csv";
        String line = "";
        String csvSplitBy = ", ";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] values = line.split(csvSplitBy);

                Item item = new Item(values[0],
                values[1],
                Boolean.parseBoolean(values[2]),
                Integer.parseInt(values[3]),
                Double.parseDouble(values[4]));
                if (itemId.equals(item.itemId)) {
                    return item;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // return null if item not found
    }
    public static void main(String[] args) {
        Item item = findItem("14"); // replace 123 with your itemId
        if (item != null) {
            System.out.println("Item found: " + item);
        } else {
            System.out.println("Item not found");
        }
    }
}
