package com.laplace.api.common.repository.db;

import com.laplace.api.common.model.db.Inquiry;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends PagingAndSortingRepository<Inquiry, Integer> {

}
