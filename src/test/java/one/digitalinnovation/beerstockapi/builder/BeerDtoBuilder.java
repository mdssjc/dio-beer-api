package one.digitalinnovation.beerstockapi.builder;

import lombok.Builder;
import one.digitalinnovation.beerstockapi.dto.BeerDto;
import one.digitalinnovation.beerstockapi.enums.BeerType;

/**
 * DTO para criação.
 *
 * @author Marcelo dos Santos
 */
@Builder
public class BeerDtoBuilder {

  @Builder.Default
  private Long id = 1L;

  @Builder.Default
  private String name = "Brahma";

  @Builder.Default
  private String brand = "Ambev";

  @Builder.Default
  private int max = 50;

  @Builder.Default
  private int quantity = 10;

  @Builder.Default
  private BeerType type = BeerType.LAGER;

  public BeerDto toBeerDto() {
    return new BeerDto(id, name, brand, max, quantity, type);
  }
}
