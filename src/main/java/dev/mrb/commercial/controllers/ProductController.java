package dev.mrb.commercial.controllers;

import dev.mrb.commercial.model.dtos.ProductDto;
import dev.mrb.commercial.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@RestController
@RequestMapping(path = "/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping(path = "/all-products")
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        Page<ProductDto> products;

        products = productService.getAllProducts(pageable);

        return products;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        ProductDto product;

        product = productService.getProduct(id);
        if (product == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping(path = "/res/add")
    public ResponseEntity<String> addProduct(@RequestBody ProductDto productDto) throws Exception {
        String status;

        status = productService.addProduct(productDto);
        if (!status.equalsIgnoreCase("ok"))
            return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(status, HttpStatus.CREATED);
    }

    @PutMapping(path = "/res/{id}/edit")
    public ResponseEntity<ProductDto> editProduct(@PathVariable Long id, @RequestBody ProductDto productDto) throws SQLException, IOException {
        ProductDto savedProduct = productService.editProduct(id, productDto);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PutMapping(path = "/res/{id}/delete")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
