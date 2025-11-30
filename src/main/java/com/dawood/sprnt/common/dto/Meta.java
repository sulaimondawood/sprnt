package com.dawood.sprnt.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Meta {

  private int currentPage;

  private int totalPages;

  private int pageSize;

  private boolean hasNext;

  private boolean hasPrev;

}
