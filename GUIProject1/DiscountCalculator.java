import java.util.Scanner;
public class DiscountCalculator {
    public static int calculateDiscount(int quantity) {
        int discount;
        if (quantity >= 1 && quantity <= 4) {
            discount = 0;
        } else if (quantity >= 5 && quantity <= 9) {
            discount = 10;
        } else if (quantity >= 10 && quantity <=14) {
            discount = 15;
        } else if (quantity >=15) {
            discount = 20;
        } else {
            throw new IllegalArgumentException("Invalid quantity");
        }
        return discount;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the quantity of an item: ");
        int quantity = scanner.nextInt();
        int discount = calculateDiscount(quantity);
        System.out.println("The discount is: " + discount + "%");
    }
}
