package one.digitalinnovation.beerstockapi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import one.digitalinnovation.beerstockapi.dto.BeerDto;
import one.digitalinnovation.beerstockapi.dto.QuantityDto;
import one.digitalinnovation.beerstockapi.exception.BeerAlreadyRegisteredException;
import one.digitalinnovation.beerstockapi.exception.BeerNotFoundException;
import one.digitalinnovation.beerstockapi.exception.BeerStockExceededException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Documentação Swagger do {@link BeerController}.
 *
 * @author Marcelo dos Santos
 */
@Api("Manages beer stock")
public interface BeerControllerDocs {

  @ApiOperation(value = "Beer creation operation")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "Success beer creation"),
      @ApiResponse(code = 400, message = "Missing required fields or wrong field range value")
  })
  BeerDto createBeer(BeerDto beerDto) throws BeerAlreadyRegisteredException;

  @ApiOperation(value = "Returns beer found by a given name")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Success beer found in the system"),
      @ApiResponse(code = 404, message = "Beer with given name not found")
  })
  BeerDto findByName(@PathVariable String name) throws BeerNotFoundException;

  @ApiOperation(value = "Returns a list of all beers registered in the system")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "List of all beers registered in the system"),
  })
  List<BeerDto> listBeers();

  @ApiOperation(value = "Delete a beer found by a given valid Id")
  @ApiResponses(value = {
      @ApiResponse(code = 204, message = "Success beer deleted in the system"),
      @ApiResponse(code = 404, message = "Beer with given id not found")
  })
  void deleteById(@PathVariable Long id) throws BeerNotFoundException;

  @ApiOperation(value = "Increment a quantity of beer found by a given valid Id")
  @ApiResponses(value = {
      @ApiResponse(code = 204, message = "Success in increasing the quantity of beer"),
      @ApiResponse(code = 400, message = "Quantity of beer above stock"),
      @ApiResponse(code = 404, message = "Beer with given id not found")
  })
  BeerDto increment(@PathVariable Long id, @RequestBody @Valid QuantityDto quantityDto)
      throws BeerNotFoundException, BeerStockExceededException;

  @ApiOperation(value = "Decrement a quantity of beer found by a given valid Id")
  @ApiResponses(value = {
      @ApiResponse(code = 204, message = "Success in decreasing the quantity of beer"),
      @ApiResponse(code = 400, message = "Quantity of beer above stock"),
      @ApiResponse(code = 404, message = "Beer with given id not found")
  })
  BeerDto decrement(@PathVariable Long id, @RequestBody @Valid QuantityDto quantityDto)
      throws BeerNotFoundException, BeerStockExceededException;
}
