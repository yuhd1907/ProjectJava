package dao;

import model.Product;
import java.util.List;

public interface IProductDAO extends IBaseDAO<Product, Integer> {
    List<Product> findByBrand(String brand);

    List<Product> findByPriceRange(double min, double max);

    List<Product> findByMinStock(int minStock);
}
