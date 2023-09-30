package com.laplace.api.common.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import lombok.Data;

@Data
public class PageableResponseDTO<T> implements Serializable {

  private static final long serialVersionUID = 2644975981508L;

  private Long total = 0L;
  private Integer totalPage = 0;
  private Collection<T> data = new HashSet<>();

  public static <T> PageableResponseDTO<T> create(Long total, Integer totalPage, Collection<T> data) {
    PageableResponseDTO<T> responseDTO = new PageableResponseDTO<>();
    responseDTO.setTotal(total);
    responseDTO.setTotalPage(totalPage);
    responseDTO.setData(data);
    return responseDTO;
  }

}