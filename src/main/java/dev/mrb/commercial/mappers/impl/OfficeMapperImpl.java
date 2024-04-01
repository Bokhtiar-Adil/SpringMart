package dev.mrb.commercial.mappers.impl;

import dev.mrb.commercial.mappers.Mapper;
import dev.mrb.commercial.model.dtos.OfficeDto;
import dev.mrb.commercial.model.entities.OfficeEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class OfficeMapperImpl implements Mapper<OfficeEntity, OfficeDto> {

    private final ModelMapper modelMapper;

    public OfficeMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public OfficeDto mapTo(OfficeEntity officeEntity) {
        return null;
    }

    @Override
    public OfficeEntity mapFrom(OfficeDto officeDto) {
        return null;
    }
}
