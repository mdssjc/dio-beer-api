package one.digitalinnovation.beerstockapi.mapper;

import one.digitalinnovation.beerstockapi.dto.BeerDto;
import one.digitalinnovation.beerstockapi.entity.Beer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapeamento entre {@link Beer} e {@link BeerDto}.
 *
 * @author Marcelo dos Santos
 */
@Mapper
public interface BeerMapper {

  BeerMapper INSTANCE = Mappers.getMapper(BeerMapper.class);

  Beer toModel(BeerDto beerDto);

  BeerDto toDto(Beer beer);
}
