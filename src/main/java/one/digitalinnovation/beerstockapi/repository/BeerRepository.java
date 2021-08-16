package one.digitalinnovation.beerstockapi.repository;

import one.digitalinnovation.beerstockapi.entity.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositório para a entidade {@link Beer}.
 *
 * @author Marcelo dos Santos
 */
public interface BeerRepository extends JpaRepository<Beer, Long> {

  Optional<Beer> findByName(String name);
}
