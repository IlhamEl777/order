# Dokumentasi Proyek Order Service

Dokumentasi ini menjelaskan arsitektur, teknologi, dan detail API dari proyek Order Service.

## Ringkasan Proyek

Proyek ini adalah sebuah layanan mikro (*microservice*) yang dirancang untuk mengelola pesanan dalam sebuah sistem E-commerce. Layanan ini menggunakan arsitektur *event-driven* untuk menangani proses pembuatan pesanan secara asinkron.

Ketika pesanan baru dibuat melalui API, layanan ini tidak langsung memprosesnya, melainkan mempublikasikan sebuah *event* ke Apache Kafka. Hal ini memungkinkan sistem untuk tetap responsif dan skalabel, di mana layanan lain (misalnya, layanan inventaris atau pembayaran) dapat berlangganan (subscribe) ke *event* tersebut dan memprosesnya secara independen.

Selain itu, proyek ini juga terintegrasi dengan Elasticsearch untuk menyediakan fungsionalitas pencarian produk yang cepat dan efisien.

## Teknologi yang Digunakan

- **Java 21**: Versi bahasa pemrograman yang digunakan.
- **Spring Boot 3**: Framework utama untuk membangun aplikasi.
- **Spring Data JPA**: Untuk berinteraksi dengan database PostgreSQL.
- **Spring Data Elasticsearch**: Untuk melakukan query ke Elasticsearch.
- **Spring for Apache Kafka**: Untuk mengirim dan menerima pesan dari Kafka.
- **Spring Data Redis**: Untuk caching dan manajemen sesi.
- **PostgreSQL**: Database relasional untuk menyimpan data utama seperti produk dan pesanan.
- **Elasticsearch**: Digunakan sebagai mesin pencari untuk data produk, memungkinkan pencarian yang cepat.
- **Redis**: Digunakan untuk caching data yang sering diakses.
- **Kafka**: Sebagai *message broker* untuk arsitektur *event-driven*.
- **Lombok**: Untuk mengurangi boilerplate code pada model dan entitas Java.
- **Gradle**: Sebagai *build tool* untuk manajemen dependensi dan proses build.

## Arsitektur & Logika

1.  **Permintaan API**: Klien mengirimkan permintaan HTTP POST ke endpoint `/api/orders` untuk membuat pesanan baru.
2.  **Controller**: `OrderController` menerima permintaan ini.
3.  **Service**: `OrderController` memanggil metode `createOrder` di `OrderService`.
4.  **Kafka Producer**: `OrderService` membuat sebuah `OrderEvent` dan mengirimkannya ke sebuah topic di Kafka.
5.  **Respon**: API segera mengembalikan pesan yang menandakan bahwa pesanan sedang diproses. Ini adalah sifat dari arsitektur *asynchronous* dan *event-driven*.

Proses selanjutnya (seperti validasi pesanan, pengurangan stok, pemrosesan pembayaran, dll.) diasumsikan akan ditangani oleh *consumer* lain yang mendengarkan *event* dari Kafka.

## Dokumentasi API

### Membuat Pesanan Baru

Membuat pesanan baru dan mempublikasikan sebuah *event* ke Kafka.

- **URL**: `/api/orders`
- **Metode**: `POST`
- **Request Body**:

  ```json
  {
    "orderId": "string",
    "itemId": "string",
    "qty": "integer"
  }
  ```

- **Contoh Request Body**:

  ```json
  {
    "orderId": "ORD-2",
    "itemId": "ITEM-1",
    "qty": 21
  }
  ```

- **Respon Sukses**:

  - **Kode**: `200 OK`
  - **Content**: `Order berhasil dibuat, sedang diproses!`

## Struktur Data

### Data Produk (Elasticsearch)

Data produk diindeks di Elasticsearch untuk pencarian cepat. Berikut adalah contoh respons dari API pencarian produk Elasticsearch.

- **URL**: `GET http://localhost:9200/products/_search`
- **Struktur Objek `_source`**:

| Field | Tipe | Deskripsi |
| :--- | :--- | :--- |
| `_class` | String | Nama kelas Java dari dokumen. |
| `id` | String | ID unik untuk produk. |
| `name` | String | Nama produk. |
| `stock`| Integer| Jumlah stok produk yang tersedia. |

- **Contoh Respons**:
  ```json
  {
    "took": 3,
    "timed_out": false,
    "_shards": {
      "total": 1,
      "successful": 1,
      "skipped": 0,
      "failed": 0
    },
    "hits": {
      "total": {
        "value": 2,
        "relation": "eq"
      },
      "max_score": 1.0,
      "hits": [
        {
          "_index": "products",
          "_type": "_doc",
          "_id": "ITEM-99",
          "_score": 1.0,
          "_source": {
            "_class": "com.learning.order.model.document.ProductDoc",
            "id": "ITEM-99",
            "name": "MacBook Pro M4",
            "stock": 99
          }
        },
        {
          "_index": "products",
          "_type": "_doc",
          "_id": "ITEM-1",
          "_score": 1.0,
          "_source": {
            "_class": "com.learning.order.model.document.ProductDoc",
            "id": "ITEM-1",
            "name": "Kopi Susu Gula Aren",
            "stock": 29
          }
        }
      ]
    }
  }
  ```
