package com.chujy.shopproject.repository;

import com.chujy.shopproject.domain.Item;
import com.chujy.shopproject.dto.ItemSearchDto;
import com.chujy.shopproject.dto.MainItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

}
