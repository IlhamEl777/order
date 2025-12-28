package com.learning.order.service;

import com.learning.order.model.document.ProductDoc;
import com.learning.order.model.dto.OrderEvent;
import com.learning.order.model.entity.Product;
import com.learning.order.repository.ProductRepository;
import com.learning.order.repository.ProductSearchRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryListener {
    private final ProductRepository productRepository;
    private ProductSearchRepository searchRepository;
    private StringRedisTemplate redisTemplate;

    public InventoryListener(ProductRepository productRepository, ProductSearchRepository searchRepository, StringRedisTemplate redisTemplate) {
        this.productRepository = productRepository;
        this.searchRepository = searchRepository;
        this.redisTemplate = redisTemplate;
    }

    @KafkaListener(topics = "topic-order", groupId = "inventory-group")
    public void handleOrder(OrderEvent order){
        //Update Postgres (Master Data)
        Product product = productRepository.findById(order.getItemId())
                .orElseThrow(()->new RuntimeException("Item tidak ditemukan"));

        product.setStock(product.getStock() - order.getQty());
        productRepository.save(product);

        //Sync Elastic agar pencarian akurat
        searchRepository.save(new ProductDoc(product));

        //Invalidate Redis (Hapus cache basi)
        String cacheKey = "stock_item_" + order.getItemId();
        redisTemplate.delete(cacheKey);

        System.out.println("Transaski Selesai");
    }
}
