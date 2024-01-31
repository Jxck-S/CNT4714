import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
class Buttons {
    public JButton exitButton;
    public JButton findItemButton;
    public JButton viewCartButton;
    public JButton emptyCartButton;
    public JButton addItemButton;
    public JButton checkoutButton;

    public Buttons(){
        initializeButtons();
    }
    public void initializeButtons() {
        exitButton = createButton("Exit");
        findItemButton = createButton(null);
        viewCartButton = createButton("View Cart");
        viewCartButton.setEnabled(false);
        emptyCartButton = createButton("Empty Cart");
        addItemButton = createButton(null);
        addItemButton.setEnabled(false);
        checkoutButton = createButton("Checkout");
        checkoutButton.setEnabled(false);
    }

    private JButton createButton(String text) {
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        Color buttonForeground = Color.BLACK;
        Color buttonBackground = Color.GRAY;
        Border buttonBorder = BorderFactory.createLineBorder(Color.BLACK, 2);

        JButton button = new JButton();
        button.setText(text);
        button.setFont(buttonFont);
        button.setForeground(buttonForeground);
        button.setBackground(buttonBackground);
        button.setBorder(buttonBorder);

        return button;
    }
}
public class ShopGUI {
    JFrame frame = new JFrame();
    Buttons buttons = new Buttons();
    JTextArea[] cartFields = new JTextArea[5];

    JTextField item_id_field = new JTextField();
    JTextField quantity_field = new JTextField();
    JTextField detials_field = new JTextField();
    JTextField subtotal_field = new JTextField();

    JLabel details_label = new JLabel();
    JLabel subt_label = new JLabel();
    JLabel item_id_label = new JLabel();
    JLabel quanity_panel_label = new JLabel();

    JLabel cart_status = new JLabel();



    private JTextArea createTextField() {
        Font font = new Font("Arial",Font.PLAIN, 14);
        JTextArea textField = new JTextArea();
        textField.setLineWrap(true);
        textField.setWrapStyleWord(true);
        textField.setFont(font);
        textField.setEditable(false);
        return textField;
    }
    public ShopGUI(int width, int height) {
        Color header_color = Color.black;
        Color header_bg = null;
        Font header_font = new Font("Arial", Font.BOLD, 18);

        frame.setTitle("Nile Shopping Store");


        //Set up a Icon for the JFrame, I defined it in BASE64 because I didn't know if we are allowed image files.
        ImageIcon icon = ImageUtils.createImageIconFromBase64(AppIcon.BASE64_ICON);
        if (icon != null) {
            frame.setIconImage(icon.getImage());
        }




        // Begin Item Panel
        JPanel item_panel = new JPanel();
        item_panel.setBackground(java.awt.Color.DARK_GRAY);

        item_panel.setLayout(new BoxLayout(item_panel, BoxLayout.Y_AXIS));
        item_panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        LayoutManager outter_layout = new GridLayout(2, 1, 2, 2);


        JPanel item_panel_items = new JPanel();
        item_panel_items.setBackground(null);
        item_panel_items.setLayout(new GridLayout(10, 1, 10, 10));


        JLabel label = new JLabel("Item Lookup");
        label.setForeground(header_color);
        label.setFont(header_font);
        label.setBackground(header_bg);
        label.setOpaque(true);

        JPanel label_panel = new JPanel();
        label_panel.add(label);
        label_panel.setMaximumSize(label_panel.getPreferredSize());


        item_panel.add(label_panel);
        item_panel.add(Box.createVerticalStrut(10));








        JPanel item_id_panel = new JPanel();
        item_id_panel.setLayout(outter_layout);
        item_id_panel.add(this.item_id_label);
        item_id_panel.add(item_id_field);


        JPanel quanity_panel = new JPanel();
        quanity_panel.setLayout(outter_layout);
        quanity_panel.add(this.quanity_panel_label);
        quanity_panel.add(this.quantity_field);


        JPanel details_panel = new JPanel();
        details_panel.setLayout(outter_layout);
        details_panel.add(this.details_label);

        detials_field.setEditable(false);
        details_panel.add(detials_field);

        JPanel subtotal_panel = new JPanel();
        subtotal_panel.setLayout(outter_layout);
        subtotal_panel.add(this.subt_label);
        subtotal_field.setEditable(false);
        subtotal_panel.add(subtotal_field);



        item_panel_items.add(item_id_panel);
        item_panel_items.add(quanity_panel);
        item_panel_items.add(details_panel);
        item_panel_items.add(subtotal_panel);
        item_panel.add(item_panel_items);


        // Begin Control Panel
        JPanel control_panel = new JPanel();
        control_panel.setBackground(java.awt.Color.LIGHT_GRAY);

        control_panel.setLayout(new BoxLayout(control_panel, BoxLayout.Y_AXIS));
        control_panel.setBorder(BorderFactory.createEmptyBorder(12, 30, 10, 30));


        JLabel control_label = new JLabel("Controls");
        control_label.setForeground(header_color);
        control_label.setFont(header_font);
        control_label.setBackground(header_bg);
        control_label.setOpaque(true);

        JPanel control_label_Panel = new JPanel();
        control_label_Panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        control_label_Panel.add(control_label);
        control_label_Panel.setMaximumSize(control_label_Panel.getPreferredSize());
        control_panel.add(control_label_Panel);
        control_panel.add(Box.createVerticalStrut(10));


        JPanel control_buttons_Panel = new JPanel();
        control_buttons_Panel.setLayout(new GridLayout(10, 1, 1, 15));
        control_buttons_Panel.setBackground(null);




        control_buttons_Panel.add(buttons.findItemButton);
        control_buttons_Panel.add(buttons.viewCartButton);
        control_buttons_Panel.add(buttons.emptyCartButton);
        control_buttons_Panel.add(buttons.addItemButton);
        control_buttons_Panel.add(buttons.checkoutButton);
        control_buttons_Panel.add(buttons.exitButton);



        control_buttons_Panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        control_panel.add(control_buttons_Panel);


        // Begin Cart Panel
        JPanel cart_panel = new JPanel();
        cart_panel.setBackground(java.awt.Color.DARK_GRAY);
        cart_panel.setLayout(new BoxLayout(cart_panel, BoxLayout.Y_AXIS));
        //cart_panel.setLayout(new GridLayout(6, 1, 10, 10));
        cart_panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JLabel cart_label = new JLabel("Shopping Cart");
        cart_label.setForeground(header_color);
        cart_label.setFont(header_font);
        cart_label.setBackground(header_bg);
        cart_label.setOpaque(true);

        JPanel cart_label_panel = new JPanel();
        cart_label_panel.add(cart_label);
        cart_label_panel.setMaximumSize(cart_label_panel.getPreferredSize());
        cart_label_panel.setAlignmentX(Component.CENTER_ALIGNMENT);


        cart_panel.add(cart_label_panel);

        cart_panel.add(Box.createVerticalStrut(5));

        this.cart_status.setForeground(Color.RED);
        this.cart_status.setAlignmentX(Component.CENTER_ALIGNMENT);



        cart_panel.add(this.cart_status);

        cart_panel.add(Box.createVerticalStrut(5));


        JPanel cart_items_Panel = new JPanel();
        cart_items_Panel.setLayout(new GridLayout(10, 1, 1, 15));
        cart_items_Panel.setBackground(null);


        // Initialize itemFields array
        for (int i = 0; i < cartFields.length; i++) {
            cartFields[i] = createTextField();
            cart_items_Panel.add(cartFields[i]);
        }
        cart_panel.add(cart_items_Panel);

        //Init counts of items on places
        this.update_current_item_count(1);
        this.update_details_counter(1);
        this.update_shop_status_label(0);


        // Get the content pane of the frame.
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new GridLayout(1, 3));
        contentPane.add(item_panel);
        contentPane.add(control_panel);
        contentPane.add(cart_panel);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        }
        public void update_current_item_count(int item_num){
            update_current_item_count(item_num, false);
        }

        public void update_current_item_count(int item_num, boolean maxed_out){
            this.item_id_label.setText(String.format("Enter Item ID for Item #%d", item_num));
            if (!maxed_out){
                this.buttons.addItemButton.setText(String.format("Add Item #%d to Cart", item_num));
                this.buttons.findItemButton.setText(String.format("Find Item #%d", item_num));
            }
            this.subt_label.setText(String.format("Current Subtotal for %d item(s)", item_num-1));
            this.quanity_panel_label.setText(String.format("Enter quantity ID for Item #%d", item_num));

        }
        public void update_details_counter(int item_num){
            this.details_label.setText(String.format("Details for Item #%d", item_num));
        }
        public void update_shop_status_label(int item_num){
            String text;
            if (item_num==0){
                text = "Your shopping cart is empty!";
            }
            else {
                text = String.format("Your Current Shopping Cart With %d Item(s)", item_num);
            }
            this.cart_status.setText(text);

        }
        public void reset(){
            this.update_current_item_count(1);
            for (int i = 0; i < this.cartFields.length; i++) {
                this.cartFields[i].setText(null);
            }
            this.detials_field.setText(null);
            this.update_details_counter(1);
            this.subtotal_field.setText(null);
            this.buttons.viewCartButton.setEnabled(false);
            this.buttons.addItemButton.setEnabled(false);
            this.item_id_field.setText(null);
            this.quantity_field.setText(null);
            this.item_id_field.setEnabled(true);
            this.quantity_field.setEnabled(true);
            this.buttons.findItemButton.setEnabled(true);
            this.buttons.checkoutButton.setEnabled(false);
            this.update_shop_status_label(0);
        }
}


