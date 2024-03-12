package eu.dar3.borsch.product;

public class Product {
    private String name;
    private String price;
    private String lastUpdated;

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
