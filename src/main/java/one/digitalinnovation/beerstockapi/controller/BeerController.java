package one.digitalinnovation.beerstockapi.controller;

import lombok.AllArgsConstructor;
import one.digitalinnovation.beerstockapi.dto.BeerDto;
import one.digitalinnovation.beerstockapi.dto.QuantityDto;
import one.digitalinnovation.beerstockapi.entity.Beer;
import one.digitalinnovation.beerstockapi.exception.BeerAlreadyRegisteredException;
import one.digitalinnovation.beerstockapi.exception.BeerNotFoundException;
import one.digitalinnovation.beerstockapi.exception.BeerStockExceededException;
import one.digitalinnovation.beerstockapi.service.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Endpoints para o gerenciamento da entidade {@link Beer}.
 *
 * @author Marcelo dos Santos
 */
@RestController
@RequestMapping("/api/v1/beers")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerController implements BeerControllerDocs {

  private final BeerService beerService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BeerDto createBeer(@RequestBody @Valid BeerDto beerDto)
      throws BeerAlreadyRegisteredException {
    return beerService.createBeer(beerDto);
  }

  @GetMapping("/{name}")
  public BeerDto findByName(@PathVariable String name) throws BeerNotFoundException {
    return beerService.findByName(name);
  }

  @GetMapping
  public List<BeerDto> listBeers() {
    return beerService.listAll();
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteById(@PathVariable Long id) throws BeerNotFoundException {
    beerService.deleteById(id);
  }

  @PatchMapping("/{id}/increment")
  public BeerDto increment(@PathVariable Long id, @RequestBody @Valid QuantityDto quantityDto)
      throws BeerNotFoundException, BeerStockExceededException {
    return beerService.increment(id, quantityDto.getQuantity());
  }
}
