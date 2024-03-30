package dev.mrb.commercial.services.impl;

import dev.mrb.commercial.mappers.Mapper;
import dev.mrb.commercial.model.dtos.ProductDto;
import dev.mrb.commercial.model.entities.ProductEntity;
import dev.mrb.commercial.repositories.ProductRepository;
import dev.mrb.commercial.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final Mapper<ProductEntity, ProductDto> productMapper;


    @Override
    public Page<ProductDto> getAllProducts(Pageable pageable) {
            return productRepository.findAll(pageable).map(productMapper::mapTo);
    }

    // fix getProduct
    @Override
    public ProductDto getProduct(Long id) {
        Optional<ProductEntity> productEntity = productRepository.findById(id);
        if (productEntity.isEmpty()) return null;
        else return productMapper.mapTo(productEntity.get());
    }

    @Override
    public ProductDto addProduct(ProductDto productDto) throws Exception {
        ProductEntity productEntity = new ProductEntity();
        if (productDto.getProductionCost() != null) productEntity.setProductionCost(productDto.getProductionCost());
        if (productDto.getName() != null) productEntity.setName(productDto.getName());
        if (productDto.getMrp() != null) productEntity.setMrp(productDto.getMrp());
        if (productDto.getBuyPrice() != null) productEntity.setBuyPrice(productDto.getBuyPrice());
        if (productDto.getDescription() != null) productEntity.setDescription(productDto.getDescription());
        if (productDto.getProductLineDesc() != null) productEntity.setProductLineDesc(productDto.getProductLineDesc());
        if (productDto.getInStock() != null) productEntity.setInStock(productDto.getInStock());
        if (productDto.getProducer() != null) productEntity.setProducer(productDto.getProducer());
        if (productDto.getSellPrice() != null) productEntity.setSellPrice(productDto.getSellPrice());
        if (productDto.getStockNum() != null) productEntity.setStockNum(productDto.getStockNum());
        if (productDto.getPhoto() != null) {
            byte[] photoBytes = productDto.getPhoto().getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            productEntity.setPhoto(photoBlob);
        }
        ProductEntity savedProductEntity = productRepository.save(productEntity);
        return productMapper.mapTo(savedProductEntity);
    }

    @Override
    public ProductDto editProduct(Long id, ProductDto productDto) throws SQLException, IOException {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(id);
        if (productDto.getProductionCost() != null) productEntity.setProductionCost(productDto.getProductionCost());
        if (productDto.getName() != null) productEntity.setName(productDto.getName());
        if (productDto.getMrp() != null) productEntity.setMrp(productDto.getMrp());
        if (productDto.getBuyPrice() != null) productEntity.setBuyPrice(productDto.getBuyPrice());
        if (productDto.getDescription() != null) productEntity.setDescription(productDto.getDescription());
        if (productDto.getProductLineDesc() != null) productEntity.setProductLineDesc(productDto.getProductLineDesc());
        if (productDto.getInStock() != null) productEntity.setInStock(productDto.getInStock());
        if (productDto.getProducer() != null) productEntity.setProducer(productDto.getProducer());
        if (productDto.getSellPrice() != null) productEntity.setSellPrice(productDto.getSellPrice());
        if (productDto.getStockNum() != null) productEntity.setStockNum(productDto.getStockNum());
        if (productDto.getPhoto() != null) {
            byte[] photoBytes = productDto.getPhoto().getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            productEntity.setPhoto(photoBlob);
        }
        ProductEntity savedProductEntity = productRepository.save(productEntity);
        return productMapper.mapTo(savedProductEntity);
    }

    @Override
    public void deleteProduct(Long id) {
        if(productRepository.existsById(id)) productRepository.deleteById(id);
    }
}
