package dev.mrb.commercial.services;

import dev.mrb.commercial.model.dtos.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public interface ProductService {
    Page<ProductDto> getAllProducts(Pageable pageable);

    ProductDto getProduct(Long id);

    String addProduct(ProductDto productDto) throws SQLException, Exception;

    String editProduct(Long id, ProductDto productDto) throws SQLException, IOException;

    void deleteProduct(Long id);
}
