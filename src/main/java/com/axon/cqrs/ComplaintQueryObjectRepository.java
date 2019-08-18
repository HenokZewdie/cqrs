package com.axon.cqrs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintQueryObjectRepository extends JpaRepository<ComplaintQueryObject, String> {

	ComplaintQueryObject findOne(String id);

}
