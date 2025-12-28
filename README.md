# Proyek Order Service

Proyek ini adalah sebuah layanan mikro (*microservice*) yang dirancang untuk mengelola pesanan dalam sebuah sistem E-commerce. Layanan ini menggunakan arsitektur *event-driven* untuk menangani proses pembuatan pesanan secara asinkron.

## Ringkasan

Ketika pesanan baru dibuat melalui API, layanan ini akan mempublikasikan sebuah *event* ke Apache Kafka. Hal ini memungkinkan sistem untuk tetap responsif dan skalabel, di mana layanan lain (misalnya, layanan inventaris atau pembayaran) dapat berlangganan (subscribe) ke *event* tersebut dan memprosesnya secara independen.

## Teknologi yang Digunakan

- **Java 21**
- **Spring Boot 3**
- **Spring Data JPA**
- **Spring Data Elasticsearch**
- **Spring for Apache Kafka**
- **Spring Data Redis**
- **PostgreSQL**
- **Elasticsearch**
- **Redis**
- **Kafka**
- **Gradle**

## Prasyarat

Pastikan Anda memiliki perangkat lunak berikut yang terinstal di sistem Anda:
- Java 21 atau lebih tinggi
- Docker dan Docker Compose (untuk menjalankan database dan message broker)

## Menjalankan Aplikasi

1.  **Clone repository ini:**
    ```bash
    git clone https://github.com/IlhamEl777/order.git
    cd order
    ```

2.  **Jalankan environment development dengan Docker Compose:**
    Perintah ini akan menjalankan container untuk PostgreSQL, Elasticsearch, Redis, dan Kafka.
    ```bash
    docker-compose up -d
    ```

3.  **Jalankan aplikasi Spring Boot:**
    Anda bisa menjalankannya melalui IDE Anda (misalnya IntelliJ IDEA atau VS Code) atau menggunakan Gradle wrapper.
    ```bash
    ./gradlew bootRun
    ```

Aplikasi akan berjalan di `http://localhost:8080`.

## Konfigurasi

Konfigurasi untuk database, Kafka, Redis, dan Elasticsearch dapat ditemukan di `src/main/resources/application.properties`.

- **PostgreSQL**: `spring.datasource.*`
- **Elasticsearch**: `spring.elasticsearch.uris`
- **Kafka**: `spring.kafka.*`
- **Redis**: `spring.data.redis.*`

## Dokumentasi Lanjutan

Untuk detail arsitektur, logika, dan dokumentasi API yang lebih lengkap, silakan lihat file [DOCUMENTATION.md](DOCUMENTATION.md).
