package one.digitalinnovation.beerstockapi.controller;

import static one.digitalinnovation.beerstockapi.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import one.digitalinnovation.beerstockapi.builder.BeerDtoBuilder;
import one.digitalinnovation.beerstockapi.dto.BeerDto;
import one.digitalinnovation.beerstockapi.dto.QuantityDto;
import one.digitalinnovation.beerstockapi.entity.Beer;
import one.digitalinnovation.beerstockapi.exception.BeerNotFoundException;
import one.digitalinnovation.beerstockapi.exception.BeerStockExceededException;
import one.digitalinnovation.beerstockapi.service.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * Teste dos endpoints de {@link Beer}.
 *
 * @author Marcelo dos Santos
 */
@ExtendWith(MockitoExtension.class)
class BeerControllerTest {

  static final String BEER_API_URL_PATH = "/api/v1/beers";
  static final long VALID_BEER_ID = 1L;
  static final long INVALID_BEER_ID = 2L;
  static final String BEER_API_SUBPATH_INCREMENT_URL = "/increment";
  static final String BEER_API_SUBPATH_DECREMENT_URL = "/decrement";

  MockMvc mockMvc;

  @Mock
  BeerService beerService;

  @InjectMocks
  BeerController beerController;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(beerController)
                             .setCustomArgumentResolvers(
                                 new PageableHandlerMethodArgumentResolver())
                             .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                             .build();
  }

  @Test
  void whenPOSTIsCalledThenABeerIsCreated() throws Exception {
    BeerDto beerDto = BeerDtoBuilder.builder().build().toBeerDto();

    when(beerService.createBeer(beerDto)).thenReturn(beerDto);

    mockMvc.perform(post(BEER_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(beerDto)))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.name", is(beerDto.getName())))
           .andExpect(jsonPath("$.brand", is(beerDto.getBrand())))
           .andExpect(jsonPath("$.type", is(beerDto.getType().toString())));
  }

  @Test
  void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
    BeerDto beerDto = BeerDtoBuilder.builder().build().toBeerDto();
    beerDto.setBrand(null);

    mockMvc.perform(post(BEER_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(beerDto)))
           .andExpect(status().isBadRequest());
  }

  @Test
  void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
    BeerDto beerDto = BeerDtoBuilder.builder().build().toBeerDto();

    when(beerService.findByName(beerDto.getName())).thenReturn(beerDto);

    mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH + "/" + beerDto.getName())
                                          .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.name", is(beerDto.getName())))
           .andExpect(jsonPath("$.brand", is(beerDto.getBrand())))
           .andExpect(jsonPath("$.type", is(beerDto.getType().toString())));
  }

  @Test
  void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
    BeerDto beerDto = BeerDtoBuilder.builder().build().toBeerDto();

    when(beerService.findByName(beerDto.getName())).thenThrow(BeerNotFoundException.class);

    mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH + "/" + beerDto.getName())
                                          .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isNotFound());
  }

  @Test
  void whenGETListWithBeersIsCalledThenOkStatusIsReturned() throws Exception {
    BeerDto beerDto = BeerDtoBuilder.builder().build().toBeerDto();

    when(beerService.listAll()).thenReturn(Collections.singletonList(beerDto));

    mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH)
                                          .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].name", is(beerDto.getName())))
           .andExpect(jsonPath("$[0].brand", is(beerDto.getBrand())))
           .andExpect(jsonPath("$[0].type", is(beerDto.getType().toString())));
  }

  @Test
  void whenGETListWithoutBeersIsCalledThenOkStatusIsReturned() throws Exception {
    BeerDto beerDto = BeerDtoBuilder.builder().build().toBeerDto();

    when(beerService.listAll()).thenReturn(Collections.singletonList(beerDto));

    mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH)
                                          .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk());
  }

  @Test
  void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
    BeerDto beerDto = BeerDtoBuilder.builder().build().toBeerDto();

    doNothing().when(beerService).deleteById(beerDto.getId());

    mockMvc.perform(MockMvcRequestBuilders.delete(BEER_API_URL_PATH + "/" + beerDto.getId())
                                          .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isNoContent());
  }

  @Test
  void whenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
    doThrow(BeerNotFoundException.class).when(beerService).deleteById(INVALID_BEER_ID);

    mockMvc.perform(MockMvcRequestBuilders.delete(BEER_API_URL_PATH + "/" + INVALID_BEER_ID)
                                          .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isNotFound());
  }

  @Test
  void whenPATCHIsCalledToIncrementDiscountThenOKstatusIsReturned() throws Exception {
    QuantityDto quantityDto = QuantityDto.builder().quantity(10).build();

    BeerDto beerDto = BeerDtoBuilder.builder().build().toBeerDto();
    beerDto.setQuantity(beerDto.getQuantity() + quantityDto.getQuantity());

    when(beerService.increment(VALID_BEER_ID, quantityDto.getQuantity())).thenReturn(beerDto);

    mockMvc.perform(patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_INCREMENT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(quantityDto))).andExpect(status().isOk())
           .andExpect(jsonPath("$.name", is(beerDto.getName())))
           .andExpect(jsonPath("$.brand", is(beerDto.getBrand())))
           .andExpect(jsonPath("$.type", is(beerDto.getType().toString())))
           .andExpect(jsonPath("$.quantity", is(beerDto.getQuantity())));
  }

  @Test
  void whenPATCHIsCalledToIncrementGreatherThanMaxThenBadRequestStatusIsReturned()
      throws Exception {
    QuantityDto quantityDto = QuantityDto.builder().quantity(30).build();

    BeerDto beerDto = BeerDtoBuilder.builder().build().toBeerDto();
    beerDto.setQuantity(beerDto.getQuantity() + quantityDto.getQuantity());

    when(beerService.increment(VALID_BEER_ID, quantityDto.getQuantity())).thenThrow(
        BeerStockExceededException.class);

    mockMvc.perform(patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_INCREMENT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(quantityDto)))
           .andExpect(status().isBadRequest());
  }

  @Test
  void whenPATCHIsCalledWithInvalidBeerIdToIncrementThenNotFoundStatusIsReturned()
      throws Exception {
    QuantityDto quantityDto = QuantityDto.builder().quantity(30).build();

    when(beerService.increment(INVALID_BEER_ID, quantityDto.getQuantity())).thenThrow(
        BeerNotFoundException.class);

    mockMvc.perform(
               patch(BEER_API_URL_PATH + "/" + INVALID_BEER_ID + BEER_API_SUBPATH_INCREMENT_URL)
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(asJsonString(quantityDto)))
           .andExpect(status().isNotFound());
  }

  @Test
  void whenPATCHIsCalledToDecrementDiscountThenOKstatusIsReturned() throws Exception {
    QuantityDto quantityDto = QuantityDto.builder().quantity(5).build();

    BeerDto beerDto = BeerDtoBuilder.builder().build().toBeerDto();
    beerDto.setQuantity(beerDto.getQuantity() + quantityDto.getQuantity());

    when(beerService.decrement(VALID_BEER_ID, quantityDto.getQuantity())).thenReturn(beerDto);

    mockMvc.perform(patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_DECREMENT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(quantityDto))).andExpect(status().isOk())
           .andExpect(jsonPath("$.name", is(beerDto.getName())))
           .andExpect(jsonPath("$.brand", is(beerDto.getBrand())))
           .andExpect(jsonPath("$.type", is(beerDto.getType().toString())))
           .andExpect(jsonPath("$.quantity", is(beerDto.getQuantity())));
  }

  @Test
  void whenPATCHIsCalledToDEcrementLowerThanZeroThenBadRequestStatusIsReturned() throws Exception {
    QuantityDto quantityDto = QuantityDto.builder().quantity(60).build();

    BeerDto beerDto = BeerDtoBuilder.builder().build().toBeerDto();
    beerDto.setQuantity(beerDto.getQuantity() + quantityDto.getQuantity());

    when(beerService.decrement(VALID_BEER_ID, quantityDto.getQuantity())).thenThrow(
        BeerStockExceededException.class);

    mockMvc.perform(patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_DECREMENT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(quantityDto))).andExpect(status().isBadRequest());
  }

  @Test
  void whenPATCHIsCalledWithInvalidBeerIdToDecrementThenNotFoundStatusIsReturned()
      throws Exception {
    QuantityDto quantityDto = QuantityDto.builder().quantity(5).build();

    when(beerService.decrement(INVALID_BEER_ID, quantityDto.getQuantity())).thenThrow(
        BeerNotFoundException.class);
    mockMvc.perform(
               patch(BEER_API_URL_PATH + "/" + INVALID_BEER_ID + BEER_API_SUBPATH_DECREMENT_URL)
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(asJsonString(quantityDto)))
           .andExpect(status().isNotFound());
  }
}
