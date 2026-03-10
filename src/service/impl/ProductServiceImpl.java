package service.impl;

import dao.IProductDAO;
import dao.impl.ProductDAOImpl;
import model.Product;
import service.IProductService;

import java.util.List;

public class ProductServiceImpl implements IProductService {
    private static final IProductDAO productDAO = new ProductDAOImpl();

    @Override
    public List<Product> findAll() {
        return productDAO.findAll();
    }

    @Override
    public Product findById(Integer id) {
        return productDAO.findById(id);
    }

    @Override
    public boolean add(Product product) {
        return productDAO.add(product);
    }

    @Override
    public boolean update(Product product) {
        return productDAO.update(product);
    }

    @Override
    public boolean delete(Integer id) {
        return productDAO.delete(id);
    }

    @Override
    public List<Product> findByBrand(String brand) {
        return productDAO.findByBrand(brand);
    }

    @Override
    public List<Product> findByPriceRange(double min, double max) {
        return productDAO.findByPriceRange(min, max);
    }

    @Override
    public List<Product> findByMinStock(int minStock) {
        return productDAO.findByMinStock(minStock);
    }
}
