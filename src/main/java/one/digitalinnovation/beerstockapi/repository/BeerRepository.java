package one.digitalinnovation.beerstockapi.repository;

import java.util.Optional;
import one.digitalinnovation.beerstockapi.entity.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório para a entidade {@link Beer}.
 *
 * @author Marcelo dos Santos
 */
public interface BeerRepository extends JpaRepository<Beer, Long> {

  Optional<Beer> findByName(String name);
}
