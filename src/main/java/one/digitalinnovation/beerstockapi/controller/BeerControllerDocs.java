package one.digitalinnovation.beerstockapi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import one.digitalinnovation.beerstockapi.dto.BeerDto;
import one.digitalinnovation.beerstockapi.exception.BeerAlreadyRegisteredException;
import one.digitalinnovation.beerstockapi.exception.BeerNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

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
      @ApiResponse(code = 400, message = "Missing required fields or wrong field range value.")
  })
  BeerDto createBeer(BeerDto beerDto) throws BeerAlreadyRegisteredException;

  @ApiOperation(value = "Returns beer found by a given name")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Success beer found in the system"),
      @ApiResponse(code = 404, message = "Beer with given name not found.")
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
      @ApiResponse(code = 404, message = "Beer with given id not found.")
  })
  void deleteById(@PathVariable Long id) throws BeerNotFoundException;
}
