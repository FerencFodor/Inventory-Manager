package com.fodor;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


public class Inventory {

    private static DecimalFormat df = new DecimalFormat("0.00");
    private Map<Integer,Product> productList = new HashMap<Integer, Product>();

    public Inventory(Map<Integer, Product> inventory){
        productList = inventory;
    }

    public Inventory(){

    }


    public void AddProduct(String name, int id, float price, int quantity) {
        Product item = new Product(name, id, price, quantity);

        if(productList.containsKey(id)){
            if (productList.get(id).getName().equalsIgnoreCase(name)) {
                System.out.println("Product already exists.");
            } else {
                System.out.println("There is already a different product with the same ID.");
            }
        } else {
            productList.put(id, item);
        }
    }

    public void RemoveProduct(int id) {
        productList.remove(id);
    }

    public  void EditName(int id, String name){
        productList.get(id).setName(name);
    }

    public void EditPrice(int id, float price){
        productList.get(id).setPrice(price);
    }

    public void EditQuantity(int id, int quantity) {
        productList.get(id).setQuantity(quantity);
    }

    public float CalculateTotalValue() {
        float totalValue = 0f;

        for (int id : productList.keySet()){
            float  price = productList.get(id).getPrice();
            int quantity = productList.get(id).getQuantity();

            totalValue += (price * (float)quantity);
        }
        return totalValue;
    }

    public int GetProductCount() {
        return productList.size();
    }

    public String PrintList(){
        StringBuilder output;
        output = new StringBuilder();
        for(int id: productList.keySet()){
            output.append("\n--------------------------------------------"
                    + "\nID:             ").append(productList.get(id).getId())
                    .append("\nName:           ").append(productList.get(id).getName())
                    .append("\nPrice per Item: €").append(df.format(productList.get(id).getPrice()))
                    .append("\nQuantity:       ").append(productList.get(id).getQuantity());
        }
        output.append("\n--------------------------------------------");
        return output.toString();
    }

    public Map<Integer, Product> getInventory() {
        return productList;
    }
}
