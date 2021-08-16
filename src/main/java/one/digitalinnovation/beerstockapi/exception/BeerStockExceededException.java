package one.digitalinnovation.beerstockapi.exception;

import java.io.Serializable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção de estoque excedente para Bad Request.
 *
 * @author Marcelo dos Santos
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BeerStockExceededException extends Exception implements Serializable {

  private static final long serialVersionUID = 42L;

  public BeerStockExceededException(Long id, int quantityToIncrement) {
    super(String.format("Beers with %s ID to increment informed exceeds the max stock capacity: %s",
                        id, quantityToIncrement));
  }
}
