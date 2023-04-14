package com.maaz.wallet.repository;

import com.maaz.wallet.model.IdempotencyTracker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdempotencyTrackerRepository extends CrudRepository<IdempotencyTracker, String> {

}
