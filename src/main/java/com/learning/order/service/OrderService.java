package com.learning.order.service;

import com.learning.order.model.dto.OrderEvent;
import com.learning.order.repository.ProductRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class OrderService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final StringRedisTemplate redisTemplate;
    private final ProductRepository productRepository;

    public OrderService(KafkaTemplate<String, Object> kafkaTemplate, StringRedisTemplate redisTemplate, ProductRepository productRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.redisTemplate = redisTemplate;
        this.productRepository = productRepository;
    }

    //cache key
    //cek stock cache
    //kalau null ambil db, set cache stock baru
    //validasi stock
    //kalau aman kirim ke kafka
    //kalau tidak aman throw exception

    public void createOrder(OrderEvent order){
        String cacheKey = "stock_item_"+order.getItemId();
        String stockStr = redisTemplate.opsForValue().get(cacheKey);

        int currentStock;

        if(stockStr != null){
            currentStock = Integer.parseInt(stockStr);
        } else{
            currentStock = productRepository.findById(order.getItemId()).get().getStock();
            redisTemplate.opsForValue().set(cacheKey, String.valueOf(currentStock), 60, TimeUnit.SECONDS);
        }

        if(currentStock >= order.getQty()){
            kafkaTemplate.send("topic-order", order);
        } else {
            throw new RuntimeException("Stock tidak cukup");
        }
        System.out.println("Order berhasil");
    }



}
