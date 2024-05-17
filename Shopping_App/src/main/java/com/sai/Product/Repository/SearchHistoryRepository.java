package com.sai.Product.Repository;

import com.sai.Product.Entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory,Long> {


}
