package com.laplace.api.common.converter.response;

import com.laplace.api.common.dto.ItemImageDTO;
import com.laplace.api.common.model.db.ItemImage;
import com.laplace.api.common.util.DateUtil;
import org.springframework.stereotype.Component;

@Component
public class ItemImageConverter {

  public ItemImage makeItemImage(ItemImageDTO dto, String itemId) {
    ItemImage itemImage = new ItemImage();
    itemImage.setId(DateUtil.getUniqueTimeBasedUUID());
    itemImage.setItemId(itemId);
    itemImage.setCoverImage(dto.getIsCoverImage());
    itemImage.setNumber(dto.getNumber());
    itemImage.setImageUrl(dto.getImageUrl());
    itemImage.setCreatedAt(DateUtil.timeNow());
    itemImage.setUpdatedAt(DateUtil.timeNow());
    return itemImage;
  }
}
