package service;

import model.Product;
import java.util.List;

public interface IProductService extends IBaseService<Product, Integer> {
    List<Product> findByBrand(String brand);

    List<Product> findByPriceRange(double min, double max);

    List<Product> findByMinStock(int minStock);
}
