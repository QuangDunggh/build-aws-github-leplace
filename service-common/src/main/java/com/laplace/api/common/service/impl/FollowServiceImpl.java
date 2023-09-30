package com.laplace.api.common.service.impl;

import com.laplace.api.common.model.db.Follow;
import com.laplace.api.common.repository.db.FollowRepository;
import com.laplace.api.common.service.FollowService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowServiceImpl implements FollowService {

  private final FollowRepository followRepository;

  @Autowired
  public FollowServiceImpl(FollowRepository followRepository) {
    this.followRepository = followRepository;
  }

  @Override
  public Optional<Follow> findByUserIdAndFollowedSellerId(Integer userId, Integer sellerId) {
    return followRepository.findByUserIdAndSellerId(userId, sellerId);
  }

  @Override
  public List<Follow> findAllByUserIdOrFollowedSellerId(Integer userId, Integer sellerId) {
    return followRepository.findAllByUserIdOrSellerId(userId, sellerId);
  }

  @Override
  public List<Follow> findAllByUserId(Integer userId) {
    return followRepository.findAllByUserId(userId);
  }

  @Override
  public List<Follow> findAllBySellerId(Integer sellerId) {
    return followRepository.findAllBySellerId(sellerId);
  }

  @Override
  public void delete(Follow follow) {
    followRepository.delete(follow);
  }

  @Override
  public Follow save(Follow follow) {
    return followRepository.save(follow);
  }
}
