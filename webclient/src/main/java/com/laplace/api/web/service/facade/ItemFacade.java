package com.laplace.api.web.service.facade;

import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemFacade {

  private final ItemService itemService;

  @Autowired
  public ItemFacade(ItemService itemService) {
    this.itemService = itemService;
  }

  public Item findItemAndValidateSeller(String id, Integer userId) {
    Item item = itemService.findById(id)
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.ITEM_NOT_FOUND));

    if (!userId.equals(item.getSellerId())) {
      throw throwApplicationException(ResultCodeConstants.UNAUTHORIZED_OPERATION);
    }
    return item;
  }
}
