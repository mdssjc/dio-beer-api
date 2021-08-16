package one.digitalinnovation.beerstockapi.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import one.digitalinnovation.beerstockapi.builder.BeerDtoBuilder;
import one.digitalinnovation.beerstockapi.dto.BeerDto;
import one.digitalinnovation.beerstockapi.entity.Beer;
import one.digitalinnovation.beerstockapi.exception.BeerAlreadyRegisteredException;
import one.digitalinnovation.beerstockapi.exception.BeerNotFoundException;
import one.digitalinnovation.beerstockapi.exception.BeerStockExceededException;
import one.digitalinnovation.beerstockapi.mapper.BeerMapper;
import one.digitalinnovation.beerstockapi.repository.BeerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Testes das regras de negócio de {@link Beer}.
 *
 * @author Marcelo dos Santos
 */
@ExtendWith(MockitoExtension.class)
class BeerServiceTest {

  static final long INVALID_BEER_ID = 1L;

  @Mock
  BeerRepository beerRepository;

  final BeerMapper beerMapper = BeerMapper.INSTANCE;

  @InjectMocks
  BeerService beerService;

  @Test
  void whenBeerInformedThenItShouldBeCreated() throws BeerAlreadyRegisteredException {
    BeerDto expectedBeerDto = BeerDtoBuilder.builder().build().toBeerDto();
    Beer expectedSavedBeer = beerMapper.toModel(expectedBeerDto);

    when(beerRepository.findByName(expectedBeerDto.getName())).thenReturn(Optional.empty());
    when(beerRepository.save(expectedSavedBeer)).thenReturn(expectedSavedBeer);

    BeerDto createdBeerDto = beerService.createBeer(expectedBeerDto);

    assertThat(createdBeerDto.getId(), is(equalTo(expectedBeerDto.getId())));
    assertThat(createdBeerDto.getName(), is(equalTo(expectedBeerDto.getName())));
    assertThat(createdBeerDto.getQuantity(), is(equalTo(expectedBeerDto.getQuantity())));
  }

  @Test
  void whenAlreadyRegisteredBeerInformedThenAnExceptionShouldBeThrown() {
    BeerDto expectedBeerDto = BeerDtoBuilder.builder().build().toBeerDto();
    Beer duplicatedBeer = beerMapper.toModel(expectedBeerDto);

    when(beerRepository.findByName(expectedBeerDto.getName())).thenReturn(
        Optional.of(duplicatedBeer));

    assertThrows(BeerAlreadyRegisteredException.class,
                 () -> beerService.createBeer(expectedBeerDto));
  }

  @Test
  void whenValidBeerNameIsGivenThenReturnABeer() throws BeerNotFoundException {
    BeerDto expectedFoundBeerDto = BeerDtoBuilder.builder().build().toBeerDto();
    Beer expectedFoundBeer = beerMapper.toModel(expectedFoundBeerDto);

    when(beerRepository.findByName(expectedFoundBeer.getName())).thenReturn(
        Optional.of(expectedFoundBeer));

    BeerDto foundBeerDto = beerService.findByName(expectedFoundBeerDto.getName());

    assertThat(foundBeerDto, is(equalTo(expectedFoundBeerDto)));
  }

  @Test
  void whenNotRegisteredBeerNameIsGivenThenThrowAnException() {
    BeerDto expectedFoundBeerDto = BeerDtoBuilder.builder().build().toBeerDto();

    when(beerRepository.findByName(expectedFoundBeerDto.getName())).thenReturn(Optional.empty());

    assertThrows(BeerNotFoundException.class,
                 () -> beerService.findByName(expectedFoundBeerDto.getName()));
  }

  @Test
  void whenListBeerIsCalledThenReturnAListOfBeers() {
    BeerDto expectedFoundBeerDto = BeerDtoBuilder.builder().build().toBeerDto();
    Beer expectedFoundBeer = beerMapper.toModel(expectedFoundBeerDto);

    when(beerRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundBeer));

    List<BeerDto> foundListBeersDto = beerService.listAll();

    assertThat(foundListBeersDto, is(not(empty())));
    assertThat(foundListBeersDto.get(0), is(equalTo(expectedFoundBeerDto)));
  }

  @Test
  void whenListBeerIsCalledThenReturnAnEmptyListOfBeers() {
    when(beerRepository.findAll()).thenReturn(Collections.emptyList());

    List<BeerDto> foundListBeersDto = beerService.listAll();

    assertThat(foundListBeersDto, is(empty()));
  }

  @Test
  void whenExclusionIsCalledWithValidIdThenABeerShouldBeDeleted() throws BeerNotFoundException {
    BeerDto expectedDeletedBeerDto = BeerDtoBuilder.builder().build().toBeerDto();
    Beer expectedDeletedBeer = beerMapper.toModel(expectedDeletedBeerDto);

    when(beerRepository.findById(expectedDeletedBeerDto.getId())).thenReturn(
        Optional.of(expectedDeletedBeer));
    doNothing().when(beerRepository).deleteById(expectedDeletedBeerDto.getId());

    beerService.deleteById(expectedDeletedBeerDto.getId());

    verify(beerRepository, times(1)).findById(expectedDeletedBeerDto.getId());
    verify(beerRepository, times(1)).deleteById(expectedDeletedBeerDto.getId());
  }

  @Test
  void whenIncrementIsCalledThenIncrementBeerStock()
      throws BeerNotFoundException, BeerStockExceededException {
    BeerDto expectedBeerDto = BeerDtoBuilder.builder().build().toBeerDto();
    Beer expectedBeer = beerMapper.toModel(expectedBeerDto);

    when(beerRepository.findById(expectedBeerDto.getId())).thenReturn(Optional.of(expectedBeer));
    when(beerRepository.save(expectedBeer)).thenReturn(expectedBeer);

    int quantityToIncrement = 10;
    int expectedQuantityAfterIncrement = expectedBeerDto.getQuantity() + quantityToIncrement;

    BeerDto incrementedBeerDto =
        beerService.increment(expectedBeerDto.getId(), quantityToIncrement);

    assertThat(expectedQuantityAfterIncrement, equalTo(incrementedBeerDto.getQuantity()));
    assertThat(expectedQuantityAfterIncrement, lessThan(expectedBeerDto.getMax()));
  }

  @Test
  void whenIncrementIsGreatherThanMaxThenThrowException() {
    BeerDto expectedBeerDto = BeerDtoBuilder.builder().build().toBeerDto();
    Beer expectedBeer = beerMapper.toModel(expectedBeerDto);

    when(beerRepository.findById(expectedBeerDto.getId())).thenReturn(Optional.of(expectedBeer));

    int quantityToIncrement = 80;
    assertThrows(BeerStockExceededException.class,
                 () -> beerService.increment(expectedBeerDto.getId(), quantityToIncrement));
  }

  @Test
  void whenIncrementAfterSumIsGreatherThanMaxThenThrowException() {
    BeerDto expectedBeerDto = BeerDtoBuilder.builder().build().toBeerDto();
    Beer expectedBeer = beerMapper.toModel(expectedBeerDto);

    when(beerRepository.findById(expectedBeerDto.getId())).thenReturn(Optional.of(expectedBeer));

    int quantityToIncrement = 45;
    assertThrows(BeerStockExceededException.class,
                 () -> beerService.increment(expectedBeerDto.getId(), quantityToIncrement));
  }

  @Test
  void whenIncrementIsCalledWithInvalidIdThenThrowException() {
    int quantityToIncrement = 10;

    when(beerRepository.findById(INVALID_BEER_ID)).thenReturn(Optional.empty());

    assertThrows(BeerNotFoundException.class,
                 () -> beerService.increment(INVALID_BEER_ID, quantityToIncrement));
  }

  @Test
  void whenDecrementIsCalledThenDecrementBeerStock()
      throws BeerNotFoundException, BeerStockExceededException {
    BeerDto expectedBeerDto = BeerDtoBuilder.builder().build().toBeerDto();
    Beer expectedBeer = beerMapper.toModel(expectedBeerDto);

    when(beerRepository.findById(expectedBeerDto.getId())).thenReturn(Optional.of(expectedBeer));
    when(beerRepository.save(expectedBeer)).thenReturn(expectedBeer);

    int quantityToDecrement = 5;
    int expectedQuantityAfterDecrement = expectedBeerDto.getQuantity() - quantityToDecrement;
    BeerDto incrementedBeerDto =
        beerService.decrement(expectedBeerDto.getId(), quantityToDecrement);

    assertThat(expectedQuantityAfterDecrement, equalTo(incrementedBeerDto.getQuantity()));
    assertThat(expectedQuantityAfterDecrement, greaterThan(0));
  }

  @Test
  void whenDecrementIsCalledToEmptyStockThenEmptyBeerStock()
      throws BeerNotFoundException, BeerStockExceededException {
    BeerDto expectedBeerDto = BeerDtoBuilder.builder().build().toBeerDto();
    Beer expectedBeer = beerMapper.toModel(expectedBeerDto);

    when(beerRepository.findById(expectedBeerDto.getId())).thenReturn(Optional.of(expectedBeer));
    when(beerRepository.save(expectedBeer)).thenReturn(expectedBeer);

    int quantityToDecrement = 10;
    int expectedQuantityAfterDecrement = expectedBeerDto.getQuantity() - quantityToDecrement;
    BeerDto incrementedBeerDto =
        beerService.decrement(expectedBeerDto.getId(), quantityToDecrement);

    assertThat(expectedQuantityAfterDecrement, equalTo(0));
    assertThat(expectedQuantityAfterDecrement, equalTo(incrementedBeerDto.getQuantity()));
  }

  @Test
  void whenDecrementIsLowerThanZeroThenThrowException() {
    BeerDto expectedBeerDto = BeerDtoBuilder.builder().build().toBeerDto();
    Beer expectedBeer = beerMapper.toModel(expectedBeerDto);

    when(beerRepository.findById(expectedBeerDto.getId())).thenReturn(Optional.of(expectedBeer));

    int quantityToDecrement = 80;
    assertThrows(BeerStockExceededException.class,
                 () -> beerService.decrement(expectedBeerDto.getId(), quantityToDecrement));
  }

  @Test
  void whenDecrementIsCalledWithInvalidIdThenThrowException() {
    int quantityToDecrement = 10;

    when(beerRepository.findById(INVALID_BEER_ID)).thenReturn(Optional.empty());

    assertThrows(BeerNotFoundException.class,
                 () -> beerService.decrement(INVALID_BEER_ID, quantityToDecrement));
  }
}
