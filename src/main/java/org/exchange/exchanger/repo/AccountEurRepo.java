package org.exchange.exchanger.repo;

import org.exchange.exchanger.entities.AccountEur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountEurRepo extends JpaRepository<AccountEur, String> {
}
