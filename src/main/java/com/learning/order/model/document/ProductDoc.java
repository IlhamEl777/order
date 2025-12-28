package com.learning.order.model.document;

import com.learning.order.model.entity.Product;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDoc {
    @Id
    private  String id;
    private String name;
    private int stock;

    public ProductDoc(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.stock = product.getStock();
    }
}
