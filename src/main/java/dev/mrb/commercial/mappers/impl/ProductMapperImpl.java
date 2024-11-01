package dev.mrb.commercial.mappers.impl;

import dev.mrb.commercial.mappers.Mapper;
import dev.mrb.commercial.model.dtos.ProductDto;
import dev.mrb.commercial.model.entities.ProductEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductMapperImpl implements Mapper<ProductEntity, ProductDto> {

    private final ModelMapper modelMapper;

    public ProductMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDto mapTo(ProductEntity productEntity) {
        ProductDto productDto;

        productDto = modelMapper.map(productEntity, ProductDto.class);

        return productDto;
    }

    @Override
    public ProductEntity mapFrom(ProductDto productDto) {
        ProductEntity productEntity;

        productEntity = modelMapper.map(productDto, ProductEntity.class);

        return productEntity;
    }
}
