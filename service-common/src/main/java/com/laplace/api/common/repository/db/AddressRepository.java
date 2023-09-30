package com.laplace.api.common.repository.db;

import com.laplace.api.common.model.db.Address;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

  Optional<Address> findByUserIdAndAsDefault(Integer userId, boolean asDefault);

  Optional<Address> findById(Integer id);

  Optional<Address> findByIdAndUserIdAndDeleted(Integer id, Integer userId, Boolean Deleted);

  List<Address> findByUserIdAndDeleted(Integer userId, Boolean Deleted);
}