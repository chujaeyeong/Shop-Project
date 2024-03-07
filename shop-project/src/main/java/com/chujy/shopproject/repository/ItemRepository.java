package com.chujy.shopproject.repository;

import com.chujy.shopproject.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
