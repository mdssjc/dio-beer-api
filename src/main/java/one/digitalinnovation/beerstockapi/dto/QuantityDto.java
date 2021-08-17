package one.digitalinnovation.beerstockapi.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
