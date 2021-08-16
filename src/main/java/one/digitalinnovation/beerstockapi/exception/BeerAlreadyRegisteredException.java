package one.digitalinnovation.beerstockapi.exception;

import java.io.Serializable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção de Beer já registrado para Bad Request.
 *
 * @author Marcelo dos Santos
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BeerAlreadyRegisteredException extends Exception implements Serializable {

  private static final long serialVersionUID = 42L;

  public BeerAlreadyRegisteredException(String beerName) {
    super(String.format("Beer with name %s already registered in the system.", beerName));
  }
}
