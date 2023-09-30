package com.laplace.api.common.service.impl;

import static com.laplace.api.common.constants.StatusConstants.DISPLAYABLE_STATUSES;
import static com.laplace.api.common.constants.StatusConstants.PICK_UP_STATUSES;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ApplicationConstants.StringUtils;
import com.laplace.api.common.constants.SearchConstants;
import com.laplace.api.common.constants.enums.ItemStatus;
import com.laplace.api.common.constants.enums.JudgementStatus;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.constants.enums.TargetAudience;
import com.laplace.api.common.converter.ItemDocumentConverter;
import com.laplace.api.common.converter.response.ItemConverter;
import com.laplace.api.common.dto.ItemSearchRequest;
import com.laplace.api.common.dto.request.ItemPackageRequestDTO;
import com.laplace.api.common.dto.request.ItemRequestDto;
import com.laplace.api.common.model.db.Brand;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.model.elasticsearch.ItemDocument;
import com.laplace.api.common.repository.db.ItemRepository;
import com.laplace.api.common.repository.elasticsearch.ItemDocumentRepository;
import com.laplace.api.common.service.AppUserService;
import com.laplace.api.common.service.BrandsService;
import com.laplace.api.common.service.ItemService;
import com.laplace.api.common.service.cache.CategoryCacheService;
import com.laplace.api.common.service.elasticsearch.ItemSearchService;
import com.laplace.api.common.util.DateUtil;
import com.laplace.api.common.util.ResourceFileLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

  private final ItemRepository itemsRepository;
  private final ItemConverter itemConverter;
  private final BrandsService brandsService;
  private final CategoryCacheService categoryCacheService;
  private final ItemDocumentConverter itemDocumentConverter;
  private final ItemDocumentRepository itemDocumentRepository;
  private final ItemSearchService itemSearchService;
  private final AppUserService appUserService;
  private final RestHighLevelClient client;

  @Autowired
  public ItemServiceImpl(ItemRepository itemsRepository,
      ItemConverter itemConverter,
      BrandsService brandsService,
      CategoryCacheService categoryCacheService,
      ItemDocumentConverter itemDocumentConverter,
      ItemDocumentRepository itemDocumentRepository,
      ItemSearchService itemSearchService,
      AppUserService appUserService,
      RestHighLevelClient client) {
    this.itemsRepository = itemsRepository;
    this.itemConverter = itemConverter;
    this.brandsService = brandsService;
    this.categoryCacheService = categoryCacheService;
    this.itemDocumentConverter = itemDocumentConverter;
    this.itemDocumentRepository = itemDocumentRepository;
    this.itemSearchService = itemSearchService;
    this.appUserService = appUserService;
    this.client = client;
  }

  @Override
  public Optional<Item> findById(String id) {
    return ObjectUtils.isEmpty(id) ? Optional.empty() : itemsRepository.findById(id);
  }

  @Override
  public Optional<Item> wmcFindById(String id) {
    return ObjectUtils.isEmpty(id) ? Optional.empty()
        : itemsRepository.findByItemIdAndBlacklistedIsFalse(id);
  }

  @Override
  public void setBlacklistedBySellerId(Integer sellerId) {
    List<Item> items = itemsRepository.findAllBySellerId(sellerId);
    items.forEach(item -> item.setBlacklisted(true));
    itemsRepository.setBlacklistedBySellerId(sellerId);
    itemDocumentRepository.saveAll(
        items.stream().map(item -> Objects.requireNonNull(itemDocumentConverter.convert(item)))
            .collect(Collectors.toList()));
  }

  @Transactional
  @Override
  public Optional<Item> findOnSaleItem(String id){
    return itemsRepository.findByItemIdAndStatusInAndBlacklistedIsFalse(id, DISPLAYABLE_STATUSES);
  }

  @Transactional
  @Override
  public Item save(Item item) {
    Item info = itemsRepository.save(item);
    itemDocumentRepository.save(Objects.requireNonNull(itemDocumentConverter.convert(item)));
    return info;
  }

  @Override
  public Item onlyItemSaveInDB(Item item) {
    return itemsRepository.save(item);
  }

  @Override
  public void syncItemInES(Item item) {
    itemDocumentRepository.save(Objects.requireNonNull(itemDocumentConverter.convert(item)));
  }

  @Override
  public List<Item> saveItems(ItemPackageRequestDTO itemPackageRequestDTO) {
    List<Item> items = itemPackageRequestDTO.getItems().stream()
        .map(itemRequestDto -> validateAndSet(itemPackageRequestDTO, itemRequestDto))
        .collect(Collectors.toList());
    return saveAll(items);
  }

  @Transactional
  @Override
  public List<Item> saveAll(List<Item> items) {
    itemsRepository.saveAll(items);
    itemDocumentRepository.saveAll(
        items.stream().map(item -> Objects.requireNonNull(itemDocumentConverter.convert(item)))
            .collect(Collectors.toList()));
    return items;
  }

  @Transactional
  @Override
  public void validateAndUpdate(Item oldItem, ItemRequestDto dto) {
    convertToItem(dto, oldItem);
    save(oldItem);
  }

  @Override
  public Page<Item> findItemByPage(String keyword, List<ItemStatus> itemStatus,
      Pageable request) {
    return !ObjectUtils.isEmpty(itemStatus) ? itemsRepository
        .findByStatusIn(itemStatus, request) :
        itemsRepository.findAll(request);
  }

  @Override
  public Page<Item> findItemsByAudience(TargetAudience targetAudience, Pageable page) {
    Set<TargetAudience> targetAudienceSet = new HashSet<>(
        Arrays.asList(targetAudience));
    return itemsRepository
        .findByTargetAudienceInAndStatusInAndHiddenIsFalseAndBlacklistedIsFalse(targetAudienceSet,
            DISPLAYABLE_STATUSES, page);
  }

  @Override
  public Page<Item> findItemsByPickUp(Pageable page) {
    return itemsRepository
        .findByPickUpIsTrueAndStatusInAndHiddenIsFalseAndBlacklistedIsFalse(PICK_UP_STATUSES, page);
  }

  @Override
  public Page<Item> findDisplayableItems(Pageable page) {
    return itemsRepository
        .findByStatusInAndHiddenIsFalseAndBlacklistedIsFalse(DISPLAYABLE_STATUSES, page);
  }

  @Override
  public Page<ItemDocument> search(ItemSearchRequest itemSearchRequest) {
    return itemSearchService.search(itemSearchRequest);
  }

  @Override
  public void sync() {
    DeleteIndexRequest deleteRequest = new DeleteIndexRequest(SearchConstants.Index.ITEMS);
    try {
      client.indices().delete(deleteRequest, RequestOptions.DEFAULT);
    } catch (Exception e) {
      log.error("++ES Schema delete error: " + e.getMessage());
    }

    CreateIndexRequest request = new CreateIndexRequest(SearchConstants.Index.ITEMS);
    String mapping = new ResourceFileLoader(SearchConstants.Mapping.ITEMS).toString();
    request = request.mapping(mapping, XContentType.JSON);
    try {
      client.indices().create(request, RequestOptions.DEFAULT);
    } catch (Exception e) {
      log.error("++ES Schema create error: " + e.getMessage());
    }

    List<Item> items = new ArrayList<>();
    itemsRepository.findAll().iterator().forEachRemaining(items::add);
    items.forEach(item -> {
      itemDocumentRepository
          .save(Objects.requireNonNull(itemDocumentConverter.convert(item)));
    });
  }

  @Override
  public void updateFavCount(String id, Item item, boolean isIncrement) {
    synchronized (id.intern()) {
      if (isIncrement) {
        item.setFavoriteCount(item.getFavoriteCount() + ApplicationConstants.ONE_LONG);
        save(item);
      } else {
        item.setFavoriteCount(item.getFavoriteCount() - ApplicationConstants.ONE_LONG);
        save(item);
      }
    }
  }

  @Override
  public Map<String, Item> findByIds(Set<String> itemIds) {
    return itemsRepository.findAllByItemIdIn(itemIds).stream()
        .collect(Collectors.toMap(Item::getItemId,
            Function.identity()));
  }

  @Override
  public void updateItemsSellerEmail(Integer sellerId, String newEmail) {
    saveAll(itemsRepository.findAllBySellerId(sellerId).stream()
        .peek(item -> item.setSellerEmail(newEmail))
        .collect(Collectors.toList())
    );
  }

  @Override
  public String findCoverImageById(String itemId) {
    return ObjectUtils.isEmpty(itemId) ? StringUtils.EMPTY_STRING
        : itemsRepository.findCoverImageByItemId(itemId);
  }

  private Item validateAndSet(ItemPackageRequestDTO itemPackageRequestDTO,
      ItemRequestDto itemRequestDto) {
    Item info = convertToItem(itemRequestDto, Item.create());
    info.setStatus(ItemStatus.WAITING_FOR_ARRIVAL);
    info.setSellerId(itemPackageRequestDTO.getUserId());
    info.setSellerEmail(itemPackageRequestDTO.getEmail());
    info.setFavoriteCount(ApplicationConstants.ZERO);
    info.setDiscountPercentage(ApplicationConstants.VALUE_ZERO);
    info.setJudgementStatus(JudgementStatus.NOT_VERIFIED);
    info.setCreatedBy(itemPackageRequestDTO.getUserId());
    info.setCreatedOn(DateUtil.timeNow());
    info.setPackageId(itemPackageRequestDTO.getPackageId());
    info.setAddressId(itemPackageRequestDTO.getAddressId());
    info.setEstimatedPickUpTimeByLaplace(itemPackageRequestDTO.getEstimatedShippingDate());
    info.setLastUpdatedBy(itemPackageRequestDTO.getUserId());
    return info;
  }

  private Item convertToItem(ItemRequestDto dto, Item item) {
    Brand brand = brandsService.findById(dto.getBrandId())
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.BRAND_NOT_FOUND));
    Pair<String, String> subCategoryPair = categoryCacheService
        .getSubCategoryByIndex(dto.getTargetAudience(), dto.getCategory(),
            dto.getSubCategoryIndex())
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.TYPE_NOT_FOUND));
    return itemConverter.makeItem(dto, item, brand, subCategoryPair);
  }
}
