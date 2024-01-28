public class ItemInCart {
    public Item item;
    public int quantity;
    public int discount_rate;
    public int spot_in_cart;
    public double subtotal;

    public ItemInCart(Item item, int quantity, int discount_rate, double subtotal) {
        this.item = item;
        this.quantity = quantity;
        this.discount_rate = discount_rate;
        this.spot_in_cart = -1;
        this.subtotal = subtotal;
    }
    public String toItemDetailsPreview(){
        String details_string = String.format("%s %s $%.2f %d %d%% $%.2f", this.item.itemId, this.item.name, this.item.price, this.quantity, this.discount_rate, this.subtotal);
        return details_string;
    }
    public String toCartView() {
        String details_string = String.format("Item %d - SKU: %s, Desc: %s, Price Ea. $%.2f, Qty: %d, Total: $%.2f", this.spot_in_cart, this.item.itemId, this.item.name, this.item.price, this.quantity, this.subtotal);
        return details_string;
    }
    public String toCartPopupView(){
        String details_string = String.format("%d. %s %s $%.2f %d %d%% $%.2f", this.spot_in_cart, this.item.itemId, this.item.name, this.item.price, this.quantity, this.discount_rate, this.subtotal);
        return details_string;
    }
}
