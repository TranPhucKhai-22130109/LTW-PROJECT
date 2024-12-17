package entity;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Cart {
    Map<Integer, CartItem> data = new HashMap<>();

    public boolean add(Product p) {
        if (data.containsKey(p.getProductID())) {
           return update(p.getProductID(), data.get(p.getProductID()).getQuantity() + 1);
        }
        // them sp moi
        data.put(p.productID, convert(p));
        return true;
    }


    public boolean update(int productID, int quantity) {
        if (!data.containsKey(productID)) return false;
        CartItem cartProduct = data.get(productID);
        if (quantity < 1) return false;
        cartProduct.setQuantity(quantity);
        data.put(productID, cartProduct);
        return true;
    }

    public boolean remove(int productID) {
        return data.remove(productID) != null;
    }

    public List<CartItem> getList() {
        return new ArrayList<>(data.values());
    }

    public int getTotalQuantity() {
        AtomicInteger i = new AtomicInteger();
        data.values().forEach(cp -> i.addAndGet(cp.getQuantity()));
        return i.get();
    }

    public Double getTotal() {
        AtomicReference<Double> total = new AtomicReference<>(0.0);
        data.values().forEach(cp -> total.updateAndGet(v -> v + (cp.getPrice() * cp.getQuantity())));
        return total.get();
    }


    public CartItem convert(Product p) {
        CartItem re = new CartItem();
        re.setId(p.getProductID());
        re.setTitle(p.getProductName());
        re.setPrice(p.getProductPrice());
        re.setImg(p.getProductImage());
        re.setQuantity(1);  // Sử dụng số lượng được truyền vào
        return re;
    }


}
