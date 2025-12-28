package com.learning.order.repository;

import com.learning.order.model.document.ProductDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories
public interface ProductSearchRepository extends ElasticsearchRepository<ProductDoc, String> {
}
