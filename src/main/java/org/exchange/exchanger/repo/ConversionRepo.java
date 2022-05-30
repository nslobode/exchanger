package org.exchange.exchanger.repo;

import org.exchange.exchanger.entities.Conversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversionRepo extends JpaRepository<Conversion, String> {
}
