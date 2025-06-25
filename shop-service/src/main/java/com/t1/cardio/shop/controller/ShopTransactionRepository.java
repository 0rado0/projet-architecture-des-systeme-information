package com.t1.cardio.shop.controller;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.t1.cardio.shop.model.ShopTransaction;
import com.t1.cardio.shop.model.ShopTransaction.Action;

public interface ShopTransactionRepository extends CrudRepository<ShopTransaction, Integer> {

    List<ShopTransaction> findAllByAction(Action sell);
 
}
