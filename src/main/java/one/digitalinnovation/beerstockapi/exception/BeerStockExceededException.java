package one.digitalinnovation.beerstockapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção de estoque excedente para Bad Request.
 *
 * @author Marcelo dos Santos
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BeerStockExceededException extends Exception {

  public BeerStockExceededException(Long id, int quantityToIncrement) {
    super(String.format("Beers with %s ID to increment informed exceeds the max stock capacity: %s", id, quantityToIncrement));
  }
}
