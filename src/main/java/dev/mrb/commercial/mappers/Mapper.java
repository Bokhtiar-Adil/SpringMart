package dev.mrb.commercial.mappers;

public interface Mapper<A,B> {

    // A -> entity, B -> dto
    B mapTo(A a); // entity to dto
    A mapFrom (B b); // dto to entity
}
