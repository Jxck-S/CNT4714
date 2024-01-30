
public class ShopInfo {
    int itemCount = 0;
    double subtotal = 0;
    int maxCount = 5;
    ItemInCart currentItem = null;
    ItemInCart[] cart = new ItemInCart[5];

    public int get_item_count(){
        return this.itemCount;
    }
    public void add_item(){
        this.cart[this.itemCount] = this.currentItem;
        this.subtotal += this.currentItem.subtotal;
        this.itemCount++;
        this.cart[this.itemCount-1].spot_in_cart = itemCount;
        this.currentItem = null;

    }
    public void reset(){
        this.itemCount = 0;
        this.subtotal = 0;
        this.currentItem = null;
        for (int i = 0; i < this.cart.length; i++) {
            this.cart[i] = null;
        }


    }
}
