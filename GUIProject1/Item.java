public class Item {
    public String itemId;
    public String name;
    public boolean inStock;
    public int stock;
    public double price;

    public Item(String itemId, String name, boolean inStock, int stock, double price) {
        this.itemId = itemId;
        this.name = name;
        this.inStock = inStock;
        this.stock = stock;
        this.price = price;
    }
    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", name='" + name + '\'' +
                ", inStock=" + inStock +
                ", stock=" + stock +
                ", price=" + price +
                '}';
    }
}
