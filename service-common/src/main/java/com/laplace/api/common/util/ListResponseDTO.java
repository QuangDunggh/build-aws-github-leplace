package com.laplace.api.common.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import lombok.Data;

@Data
public class ListResponseDTO<T, E> implements Serializable {

  private static final long serialVersionUID = 2644975981508L;

  private Long total = 0L;
  private E nextPivot;
  private Collection<T> data = new HashSet<>();
}