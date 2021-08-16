package one.digitalinnovation.beerstockapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * DTO para extração da quantidade.
 *
 * @author Marcelo dos Santos
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuantityDto {

  @NotNull
  @Max(100)
  private Integer quantity;
}
