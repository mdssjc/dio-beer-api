package one.digitalinnovation.beerstockapi.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import one.digitalinnovation.beerstockapi.dto.BeerDto;
import one.digitalinnovation.beerstockapi.dto.QuantityDto;
import one.digitalinnovation.beerstockapi.entity.Beer;
import one.digitalinnovation.beerstockapi.exception.BeerAlreadyRegisteredException;
import one.digitalinnovation.beerstockapi.exception.BeerNotFoundException;
import one.digitalinnovation.beerstockapi.exception.BeerStockExceededException;
import one.digitalinnovation.beerstockapi.service.BeerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints para o gerenciamento da entidade {@link Beer}.
 *
 * @author Marcelo dos Santos
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/beers")
public class BeerController implements BeerControllerDocs {

  private final BeerService beerService;

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BeerDto createBeer(@RequestBody @Valid BeerDto beerDto)
      throws BeerAlreadyRegisteredException {
    return beerService.createBeer(beerDto);
  }

  @Override
  @GetMapping("/{name}")
  public BeerDto findByName(@PathVariable String name) throws BeerNotFoundException {
    return beerService.findByName(name);
  }

  @Override
  @GetMapping
  public List<BeerDto> listBeers() {
    return beerService.listAll();
  }

  @Override
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteById(@PathVariable Long id) throws BeerNotFoundException {
    beerService.deleteById(id);
  }

  @Override
  @PatchMapping("/{id}/increment")
  public BeerDto increment(@PathVariable Long id, @RequestBody @Valid QuantityDto quantityDto)
      throws BeerNotFoundException, BeerStockExceededException {
    return beerService.increment(id, quantityDto.getQuantity());
  }

  @Override
  @PatchMapping("/{id}/decrement")
  public BeerDto decrement(@PathVariable Long id, @RequestBody @Valid QuantityDto quantityDto)
      throws BeerNotFoundException, BeerStockExceededException {
    return beerService.decrement(id, quantityDto.getQuantity());
  }
}
