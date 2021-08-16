package one.digitalinnovation.beerstockapi.service;

import lombok.AllArgsConstructor;
import one.digitalinnovation.beerstockapi.dto.BeerDto;
import one.digitalinnovation.beerstockapi.entity.Beer;
import one.digitalinnovation.beerstockapi.exception.BeerAlreadyRegisteredException;
import one.digitalinnovation.beerstockapi.exception.BeerNotFoundException;
import one.digitalinnovation.beerstockapi.exception.BeerStockExceededException;
import one.digitalinnovation.beerstockapi.mapper.BeerMapper;
import one.digitalinnovation.beerstockapi.repository.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Regras de negócio para a entidade {@link Beer}.
 *
 * @author Marcelo dos Santos
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerService {

  private static final BeerMapper BEER_MAPPER = BeerMapper.INSTANCE;
  private final BeerRepository beerRepository;

  public BeerDto createBeer(BeerDto beerDto) throws BeerAlreadyRegisteredException {
    verifyIfIsAlreadyRegistered(beerDto.getName());
    Beer beer = BEER_MAPPER.toModel(beerDto);
    Beer savedBeer = beerRepository.save(beer);
    return BEER_MAPPER.toDto(savedBeer);
  }

  public BeerDto findByName(String name) throws BeerNotFoundException {
    Beer foundBeer = beerRepository.findByName(name)
                                   .orElseThrow(() -> new BeerNotFoundException(name));
    return BEER_MAPPER.toDto(foundBeer);
  }

  public List<BeerDto> listAll() {
    return beerRepository.findAll()
                         .stream()
                         .map(BEER_MAPPER::toDto)
                         .collect(Collectors.toList());
  }

  public void deleteById(Long id) throws BeerNotFoundException {
    verifyIfExists(id);
    beerRepository.deleteById(id);
  }

  private void verifyIfIsAlreadyRegistered(String name) throws BeerAlreadyRegisteredException {
    Optional<Beer> optSavedBeer = beerRepository.findByName(name);
    if (optSavedBeer.isPresent()) {
      throw new BeerAlreadyRegisteredException(name);
    }
  }

  private Beer verifyIfExists(Long id) throws BeerNotFoundException {
    return beerRepository.findById(id)
                         .orElseThrow(() -> new BeerNotFoundException(id));
  }

  public BeerDto increment(Long id, int quantity) throws BeerNotFoundException, BeerStockExceededException {
    Beer beerToIncrementStock = verifyIfExists(id);
    int quantityAfterIncrement = quantity + beerToIncrementStock.getQuantity();
    if (quantityAfterIncrement <= beerToIncrementStock.getMax()) {
      beerToIncrementStock.setQuantity(beerToIncrementStock.getQuantity() + quantity);
      Beer incrementedBeerStock = beerRepository.save(beerToIncrementStock);
      return BEER_MAPPER.toDto(incrementedBeerStock);
    }
    throw new BeerStockExceededException(id, quantity);
  }

  public BeerDto decrement(long id, Integer quantity) throws BeerNotFoundException, BeerStockExceededException {
    Beer beerToDecrementStock = verifyIfExists(id);
    int quantityAfterDecrement = quantity + beerToDecrementStock.getQuantity();
    if (quantityAfterDecrement <= beerToDecrementStock.getMax()) {
      beerToDecrementStock.setQuantity(beerToDecrementStock.getQuantity() - quantity);
      Beer decrementedBeerStock = beerRepository.save(beerToDecrementStock);
      return BEER_MAPPER.toDto(decrementedBeerStock);
    }
    throw new BeerStockExceededException(id, quantity);
  }
}
