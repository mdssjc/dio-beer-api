package one.digitalinnovation.beerstockapi.exception;

import java.io.Serializable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção de Beer não encontrado para Not Found.
 *
 * @author Marcelo dos Santos
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class BeerNotFoundException extends Exception implements Serializable {

  private static final long serialVersionUID = 42L;

  public BeerNotFoundException(String beerName) {
    super(String.format("Beer with name %s not found in the system.", beerName));
  }

  public BeerNotFoundException(Long id) {
    super(String.format("Beer with id %s not found in the system.", id));
  }
}
