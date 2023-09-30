package com.laplace.api.common.service;

import com.laplace.api.common.constants.enums.ItemStatus;
import com.laplace.api.common.constants.enums.TargetAudience;
import com.laplace.api.common.dto.ItemSearchRequest;
import com.laplace.api.common.dto.request.ItemPackageRequestDTO;
import com.laplace.api.common.dto.request.ItemRequestDto;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.model.elasticsearch.ItemDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ItemService {

  Optional<Item> findById(String id);

  Optional<Item> wmcFindById(String id);

  void setBlacklistedBySellerId(Integer sellerId);

  Item save(Item item);

  Item onlyItemSaveInDB(Item item);

  List<Item> saveAll(List<Item> item);

  void syncItemInES(Item item);

  List<Item> saveItems(ItemPackageRequestDTO itemPackageRequestDTO);

  void validateAndUpdate(Item oldItem, ItemRequestDto dto);

  Page<Item> findItemByPage(String keyword, List<ItemStatus> itemStatus, Pageable request);

  Page<Item> findItemsByAudience(TargetAudience targetAudience, Pageable page);

  Page<Item> findItemsByPickUp(Pageable page);

  Page<Item> findDisplayableItems(Pageable page);

  Optional<Item> findOnSaleItem(String id);

  Page<ItemDocument> search(ItemSearchRequest itemSearchRequest);

  void sync();

  void updateFavCount(String id, Item item, boolean isIncrement);

  Map<String, Item> findByIds(Set<String> itemIds);

  void updateItemsSellerEmail(Integer sellerId, String newEmail);

  String findCoverImageById(String itemId);
}
