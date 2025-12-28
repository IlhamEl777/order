package com.learning.order.util;

import com.learning.order.model.document.ProductDoc;
import com.learning.order.model.entity.Product;
import com.learning.order.repository.ProductRepository;
import com.learning.order.repository.ProductSearchRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
    private final ProductRepository productRepository;
    private final ProductSearchRepository searchRepository;

    public DataSeeder(ProductRepository productRepository, ProductSearchRepository searchRepository) {
        this.productRepository = productRepository;
        this.searchRepository = searchRepository;
    }

    @Override
    public void run(String... args) {
        //resetData
        productRepository.deleteAll();
        searchRepository.deleteAll();

        //buat product dummy
        Product p1 = new Product("ITEM-99", "MacBook Pro M4", 100);
        Product p2 = new Product("ITEM-1", "Kopi Susu Gula Aren", 50);

        //simpan ke db
        productRepository.save(p1);
        productRepository.save(p2);

        //simpan ke elastic
        searchRepository.save(new ProductDoc(p1));
        searchRepository.save(new ProductDoc(p2));

        System.out.println("-- DATA DUMMY BERHASIL DI_LOAD --");
    }
}
