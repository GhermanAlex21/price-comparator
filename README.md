# Price Comparator - Market

A Java Spring Boot application that compares grocery prices across multiple supermarkets (Lidl, Kaufland, Profi) using data imported from CSV files. The application supports features such as product price tracking, discount analysis, shopping basket optimization, user alerts, and recommendations.

---

## 🔎 Overview
This app allows users to:
- Import product and discount data from structured CSV files.
- Track price history per product and filter it.
- Find best discounts available on a given day.
- Compare value per unit across similar products.
- Get product recommendations within a category.
- Optimize a shopping basket by selecting the lowest total cost combination.
- Create alerts for price drops.
- Automate discount and alert checks with scheduled tasks.

---

## ⚙️ Tech Stack
- Java 17
- Spring Boot 3.4.5
- Spring Data JPA
- PostgreSQL
- OpenCSV
- Lombok

---

## 📂 Project Structure
```
com.pricecomparator.market
├── controller
├── dto
├── importer
├── model
├── repository
├── service
├── PriceComparatorMarketApplication.java
```

---

## ✅ Features & Endpoints

### 1. **Product Price History**
- `GET /api/prices/history/{productId}` – Full price history.
- `GET /api/prices/history/filter?productId=&supermarket=&from=&to=` – Filtered price history.

### 2. **Best Discounts**
- `GET /api/discounts/best` – Best discounts active today.
- `GET /api/discounts/new` – Newly added discounts in the past 24h.

### 3. **Discounts by Supermarket** 
- `GET /api/discounts/supermarket?name=Lidl`

### 4. **Value per Unit**
- `GET /api/prices/value-per-unit?category=cafea`

### 5. **Recommendations**
- `GET /api/prices/recommendations?category=bauturi`

### 6. **Alert Management**
- `POST /api/alerts`
```json
{
  "productId": 15,
  "email": "example@email.com",
  "targetPrice": 20.00
}
```
- `GET /api/alerts/matches`
- `GET /api/alerts/active`
- `DELETE /api/alerts/{id}`
- `POST /api/alerts/check`

### 7. **Shopping Basket Optimization**
- `POST /api/basket/optimize`
```json
[
  { "productId": 1, "quantity": 2 },
  { "productId": 5, "quantity": 1 }
]
```

---

## 🚀 How to Build & Run
1. Make sure PostgreSQL is installed and running.
2. Clone the repository:
```bash
git clone https://github.com/your-username/price-comparator.git
cd price-comparator
```
3. Create a PostgreSQL database named `price_comparator`.
4. Update `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/price_comparator
spring.datasource.username=your_user
spring.datasource.password=your_password
```
5. Build and run the app:
```bash
mvn clean install
mvn spring-boot:run
```

---

## 📂 CSV Import Logic
- Files placed in `src/main/resources/csv/` are auto-imported at startup.
- Products and discounts are identified by content structure, not filename.
- Duplicate checks prevent re-importing identical data.

---

## 🧪 Sample Data
- Multiple product and discount CSVs are used.
- Custom CSVs with overlapping products, price variations, and real-like data are included to simulate a real use case.

---

## 📅 Scheduled Tasks
- Daily discount and alert scan.
- Trigger alert matches and log them.
