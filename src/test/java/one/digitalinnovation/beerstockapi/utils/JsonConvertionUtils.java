package one.digitalinnovation.beerstockapi.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utilitário para conversão para JSON.
 *
 * @author Marcelo dos Santos
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonConvertionUtils {

  public static String asJsonString(Object bookDto) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      objectMapper.registerModules(new JavaTimeModule());

      return objectMapper.writeValueAsString(bookDto);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
