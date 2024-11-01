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
        Optional<ProductEntity> productEntity;
        
        productEntity = productRepository.findById(id);
        if (productEntity.isEmpty()) 
            return null;
        
        return productMapper.mapTo(productEntity.get());
    }

    @Override
    public String addProduct(ProductDto productDto) throws Exception {
        ProductEntity productEntity;

        productEntity = new ProductEntity();
        if (productDto.getName() != null)
            productEntity.setName(productDto.getName());
        if (productDto.getProductLineDesc() != null)
            productEntity.setProductLineDesc(productDto.getProductLineDesc());
        if (productDto.getStockNum() != null)
            productEntity.setStockNum(productDto.getStockNum());
        if (productDto.getProducer() != null)
            productEntity.setProducer(productDto.getProducer());
        if (productDto.getDescription() != null)
            productEntity.setDescription(productDto.getDescription());
        if (productDto.getQuantityInStock() != null)
            productEntity.setQuantityInStock(productDto.getQuantityInStock());
        if (productDto.getProductionCost() != null) 
            productEntity.setProductionCost(productDto.getProductionCost());
        if (productDto.getBuyPrice() != null)
            productEntity.setBuyPrice(productDto.getBuyPrice());
        if (productDto.getSellPrice() != null)
            productEntity.setSellPrice(productDto.getSellPrice());
        if (productDto.getMrp() != null) 
            productEntity.setMrp(productDto.getMrp());       
        if (productDto.getPhotoUrl() != null) {
            byte[] photoBytes = productDto.getPhotoUrl().getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            productEntity.setPhotoUrl(photoBlob.toString());
        }
        
        productRepository.save(productEntity);
        
        return "ok";
    }

    @Override
    public String editProduct(Long id, ProductDto productDto) throws SQLException, IOException {
        Optional<ProductEntity> productEntity;

        productEntity = productRepository.findById(id);
        if (productEntity.isEmpty())
            return "Invalid product id";

        if (productDto.getName() != null)
            productEntity.get().setName(productDto.getName());
        if (productDto.getProductLineDesc() != null)
            productEntity.get().setProductLineDesc(productDto.getProductLineDesc());
        if (productDto.getStockNum() != null)
            productEntity.get().setStockNum(productDto.getStockNum());
        if (productDto.getProducer() != null)
            productEntity.get().setProducer(productDto.getProducer());
        if (productDto.getDescription() != null)
            productEntity.get().setDescription(productDto.getDescription());
        if (productDto.getQuantityInStock() != null)
            productEntity.get().setQuantityInStock(productDto.getQuantityInStock());
        if (productDto.getProductionCost() != null)
            productEntity.get().setProductionCost(productDto.getProductionCost());
        if (productDto.getBuyPrice() != null)
            productEntity.get().setBuyPrice(productDto.getBuyPrice());
        if (productDto.getSellPrice() != null)
            productEntity.get().setSellPrice(productDto.getSellPrice());
        if (productDto.getMrp() != null)
            productEntity.get().setMrp(productDto.getMrp());
        if (productDto.getPhotoUrl() != null) {
            byte[] photoBytes = productDto.getPhotoUrl().getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            productEntity.get().setPhotoUrl(photoBlob.toString());
        }
        productRepository.save(productEntity.get());

        return "ok";
    }

    @Override
    public void deleteProduct(Long id) {
        if(productRepository.existsById(id))
            productRepository.deleteById(id);
    }
}
