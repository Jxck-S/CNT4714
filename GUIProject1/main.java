/* Name: Jack Sweeney
Course: CNT 4714 – Spring 2024
Assignment title: Project 1 – An Event-driven Enterprise Simulation
Date: Sunday January 28, 2024
*/

public class main {
    public static void main(String[] args) {
        int width = 1000;
        int height = 700;
        ShopGUI shop_gui = new ShopGUI(width, height);
        ShopInfo shop_info = new ShopInfo();

        shop_gui.buttons.exitButton.addActionListener(new ShopListeners.ExitListener());
        shop_gui.buttons.findItemButton.addActionListener(new ShopListeners.FindItemListener(shop_gui, shop_info));
        shop_gui.buttons.addItemButton.addActionListener(new ShopListeners.AddItemListener(shop_gui, shop_info));
        shop_gui.buttons.emptyCartButton.addActionListener(new ShopListeners.EmptyCartListener(shop_gui, shop_info));
        shop_gui.buttons.viewCartButton.addActionListener(new ShopListeners.ViewCartListener(shop_gui, shop_info));
        shop_gui.buttons.checkoutButton.addActionListener(new ShopListeners.CheckoutListener(shop_gui, shop_info));

        shop_gui.frame.setVisible(true);
    }
}
