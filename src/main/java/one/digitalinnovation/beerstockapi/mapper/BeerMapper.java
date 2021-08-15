package one.digitalinnovation.beerstockapi.mapper;

import one.digitalinnovation.beerstockapi.dto.BeerDTO;
import one.digitalinnovation.beerstockapi.entity.Beer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BeerMapper {

    BeerMapper INSTANCE = Mappers.getMapper(BeerMapper.class);

    Beer toModel(BeerDTO beerDTO);

    BeerDTO toDTO(Beer beer);
}
