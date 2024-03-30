package dev.mrb.commercial.mappers.impl;

import dev.mrb.commercial.mappers.Mapper;
import dev.mrb.commercial.model.dtos.OfficeDto;
import dev.mrb.commercial.model.entities.OfficeEntity;

public class OfficeMapperImpl implements Mapper<OfficeEntity, OfficeDto> {
    @Override
    public OfficeDto mapTo(OfficeEntity officeEntity) {
        return null;
    }

    @Override
    public OfficeEntity mapFrom(OfficeDto officeDto) {
        return null;
    }
}
