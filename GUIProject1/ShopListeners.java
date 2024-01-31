import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
public class ShopListeners {
        static public class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.out.println("Exiting");
            System.exit(0);
        }
    }
    static public class FindItemListener implements ActionListener {
        private ShopGUI shop_gui;
        private ShopInfo shop_info;
        public FindItemListener(ShopGUI shop_gui, ShopInfo shop_info) {
            this.shop_gui = shop_gui;
            this.shop_info = shop_info;
        }
        public void actionPerformed(ActionEvent e) {
            String item_id_str = this.shop_gui.item_id_field.getText();
            // Checks for no item ID at all
            if (item_id_str.strip().equals("")){
                JOptionPane.showMessageDialog(null, "Enter an Item ID!", "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            System.out.println("Finding Item: " + item_id_str);
            Item item = Inventory.findItem(item_id_str);
            if (item != null){
                System.out.println("Item Found "+item);
                if (!item.inStock){
                    JOptionPane.showMessageDialog(null, "Sorry item out of stock, please try another item.", "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
                    shop_gui.item_id_field.setText(null);
                    shop_gui.quantity_field.setText(null);
                    return;
                }
                int quantity;
                String quanity_str = this.shop_gui.quantity_field.getText();
                try {
                    quantity = Integer.parseInt(quanity_str);
                }
                // Catches bad quantities
                catch (NumberFormatException error) {
                    System.out.println("Enter a valid quantity");
                    JOptionPane.showMessageDialog(null, "Enter a valid quantity", "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int discount = DiscountCalculator.calculateDiscount(quantity);
                double discount_amt = 0.0;
                double w_out_discount_subt = (quantity * item.price);
                if (discount > 0){
                    discount_amt = (w_out_discount_subt * ((double)discount/100));
                    System.out.println(discount_amt);
                }
                double item_subtotal = (quantity * item.price) - discount_amt;
                this.shop_info.currentItem = new ItemInCart(item, quantity, discount, item_subtotal);
                String details_preview =  this.shop_info.currentItem.toItemDetailsPreview();
                System.out.println(details_preview);
                shop_gui.detials_field.setText(details_preview);
                shop_gui.buttons.findItemButton.setEnabled(false);
                shop_gui.buttons.addItemButton.setEnabled(true);
                shop_gui.update_details_counter(shop_info.get_item_count()+1);
            }
            //Item not found
            else {
                System.out.println("Item not found "+item_id_str);;
                JOptionPane.showMessageDialog(null, "item ID "+item_id_str+" not in file", "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }
    static public class AddItemListener implements ActionListener {
        private ShopGUI shop_gui;
        private ShopInfo shop_info;
        public AddItemListener(ShopGUI shop_gui, ShopInfo shop_info) {
            this.shop_info = shop_info;
            this.shop_gui = shop_gui;
        }
        public void actionPerformed(ActionEvent e) {
            //Update quantity if changed since find lookup/not enough stock
            String quanity_str = this.shop_gui.quantity_field.getText();
            try {
                this.shop_info.currentItem.quantity = Integer.parseInt(quanity_str);
            }
            // Catches bad quantities
            catch (NumberFormatException error) {
                System.out.println("Enter a valid quantity");
                JOptionPane.showMessageDialog(null, "Enter a valid quantity", "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int discount = DiscountCalculator.calculateDiscount(this.shop_info.currentItem.quantity);
            double discount_amt = 0.0;
            double w_out_discount_subt = (this.shop_info.currentItem.quantity  * this.shop_info.currentItem.item.price);
            if (discount > 0){
                discount_amt = (w_out_discount_subt * ((double)discount/100));
                System.out.println(discount_amt);
            }
            this.shop_info.currentItem.discount_rate = discount;
            this.shop_info.currentItem.subtotal = (this.shop_info.currentItem.quantity  * this.shop_info.currentItem.item.price) - discount_amt;
            // Not enough stock check
            if (this.shop_info.currentItem.quantity > this.shop_info.currentItem.item.stock){
                String err_mesage = String.format("Insufficient stock. Only %d on hand. Please reduce the quantity.", this.shop_info.currentItem.item.stock);
                System.out.println(err_mesage);
                JOptionPane.showMessageDialog(null, err_mesage, "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            this.shop_info.add_item();
            int idx_spot_in_cart = this.shop_info.get_item_count()-1;
            this.shop_gui.cartFields[idx_spot_in_cart].setText(this.shop_info.cart[idx_spot_in_cart].toCartView());
            this.shop_gui.buttons.addItemButton.setEnabled(false);
            System.out.println("Added to cart " + this.shop_info.cart[idx_spot_in_cart].toCartView());
            boolean maxed_out;
            if (this.shop_info.itemCount < this.shop_info.maxCount){
                this.shop_gui.buttons.findItemButton.setEnabled(true);
                maxed_out = false;
            } else {
                System.out.println("Cart is now full");
                maxed_out = true;
            }
            this.shop_gui.item_id_field.setText(null);
            this.shop_gui.quantity_field.setText(null);
            this.shop_gui.subtotal_field.setText(String.format("$%.2f", this.shop_info.subtotal));
            this.shop_gui.update_current_item_count(shop_info.get_item_count()+1, maxed_out);
            this.shop_gui.buttons.viewCartButton.setEnabled(true);
            this.shop_gui.buttons.checkoutButton.setEnabled(true);
            this.shop_gui.update_shop_status_label(this.shop_info.get_item_count());
        }
    }
    static public class EmptyCartListener implements ActionListener {
        private ShopGUI shop_gui;
        private ShopInfo shop_info;
        public EmptyCartListener(ShopGUI shop_gui, ShopInfo shop_info) {
            this.shop_gui = shop_gui;
            this.shop_info = shop_info;
        }
        public void actionPerformed(ActionEvent e) {
            this.shop_info.reset();
            this.shop_gui.reset();
        }

    }
    static public class ViewCartListener implements ActionListener {
        private ShopGUI shop_gui;
        private ShopInfo shop_info;
        public ViewCartListener(ShopGUI shop_gui, ShopInfo shop_info) {
            this.shop_gui = shop_gui;
            this.shop_info = shop_info;
        }
        public void actionPerformed(ActionEvent e) {
            String cart_preview_body = "";
            for (int i = 0; i < shop_info.itemCount; i++) {
                cart_preview_body += shop_info.cart[i].toCartPopupView()+"\n";
            }

            JOptionPane.showMessageDialog(null, cart_preview_body, "Nile Dot Com - Current Shopping Cart Status", JOptionPane.PLAIN_MESSAGE);

        }

    }
    static public class CheckoutListener implements ActionListener {
        private ShopGUI shop_gui;
        private ShopInfo shop_info;
        public CheckoutListener(ShopGUI shop_gui, ShopInfo shop_info) {
            this.shop_gui = shop_gui;
            this.shop_info = shop_info;
        }

        public void actionPerformed(ActionEvent e) {
            String checkout_items = "";
            ZonedDateTime now = ZonedDateTime.now();
            for (int i = 0; i < shop_info.itemCount; i++) {
                ItemInCart current_ItemInCart = shop_info.cart[i];
                String item_num_str = Integer.toString(current_ItemInCart.spot_in_cart);
                checkout_items +=  item_num_str+". "+current_ItemInCart.toItemDetailsPreview()+"\n";
                AddTransaction.addTransaction(current_ItemInCart, now);
            }
            // Get the current date and time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy, h:mm:ss a z");
            String formattedDateTime = now.format(formatter);

            int tax_rate = 6;
            double tax_amt = (double)shop_info.subtotal * ((double)tax_rate/100);
            double total = shop_info.subtotal + tax_amt;
            // Create the invoice string
            String invoiceString = "INVOICE\n" + "\n"
                    + "Date: " + formattedDateTime + "\n" + "\n"
                    + String.format("Number of line items: %d\n", shop_info.itemCount) + "\n"
                    + "Item# / ID / Title / Price / Qty / Disc % / Subtotal\n\n"
                    + checkout_items + "\n"
                    + String.format("Order subtotal: $%,.2f\n", shop_info.subtotal)
                    + String.format("Tax rate: %d%%\n", tax_rate) + "\n"
                    + String.format("Tax amount: $%.2f\n", tax_amt) + "\n"
                    + String.format("ORDER TOTAL: $%,.2f\n", total) + "\n"
                    + "Thanks for shopping at Nile Dot Com!";

            // Display the invoice string in a pop-up dialog
            JOptionPane.showMessageDialog(null, invoiceString, "Nile Dot Com - FINAL INVOICE", JOptionPane.PLAIN_MESSAGE);
            this.shop_gui.item_id_field.setEnabled(false);
            this.shop_gui.quantity_field.setEnabled(false);

        }

    }

}
