package one.digitalinnovation.beerstockapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import one.digitalinnovation.beerstockapi.entity.Beer;

/**
 * Representa os tipos de {@link Beer}.
 *
 * @author Marcelo dos Santos
 */
@Getter
@AllArgsConstructor
public enum BeerType {

  LAGER("Lager"),
  MALZBIER("Malzbier"),
  WITBIER("Witbier"),
  WEISS("Weiss"),
  ALE("Ale"),
  IPA("IPA"),
  STOUT("Stout");

  private final String description;
}
