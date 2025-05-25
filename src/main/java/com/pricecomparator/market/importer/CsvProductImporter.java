package com.pricecomparator.market.importer;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.pricecomparator.market.model.*;
import com.pricecomparator.market.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvProductImporter {

    private final ProductRepository productRepository;
    private final SupermarketRepository supermarketRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final PriceRepository priceRepository;

    public void importAllFromFolder(String folderPath) throws IOException, CsvValidationException {
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("⚠️ Invalid folder: " + folderPath);
            return;
        }

        File[] csvFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
        if (csvFiles == null) return;

        for (File file : csvFiles) {
            try (CSVReader reader = new CSVReaderBuilder(new FileReader(file))
                    .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                    .build()) {

                String[] header = reader.readNext();

                if (!isValidProductCsvHeader(header)) {
                    System.out.println("❌ File ignored (invalid header): " + file.getName());
                    continue;
                }

                String fileName = file.getName().toLowerCase();
                String[] parts = fileName.replace(".csv", "").split("_");
                if (parts.length < 2) {
                    System.out.println("❌ File ignored (invalid name format): " + fileName);
                    continue;
                }

                String supermarketName = capitalizeFirstLetter(parts[0]);
                LocalDate fileDate = LocalDate.parse(parts[1]);

                System.out.println("✅ Importing file: " + fileName);
                importFile(file.getAbsolutePath(), supermarketName, fileDate);

            } catch (Exception e) {
                System.out.println("❌ Error while processing file " + file.getName() + ": " + e.getMessage());
            }
        }
    }

    private boolean isValidProductCsvHeader(String[] header) {
        if (header == null || header.length < 8) return false;

        List<String> expectedHeaders = List.of(
                "product_id", "product_name", "product_category", "brand",
                "package_quantity", "package_unit", "price", "currency"
        );

        for (String expected : expectedHeaders) {
            boolean found = false;
            for (String col : header) {
                if (col.trim().equalsIgnoreCase(expected)) {
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }

        return true;
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public void importFile(String filePath, String supermarketName, LocalDate fileDate) throws IOException, CsvValidationException {
        Supermarket supermarket = supermarketRepository
                .findByNameIgnoreCase(supermarketName)
                .orElseGet(() -> supermarketRepository.save(new Supermarket(null, supermarketName)));

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {

            String[] row;
            boolean skipHeader = true;

            while ((row = reader.readNext()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                if (row.length < 8) {
                    System.out.println("⚠️ Row ignored (not enough columns): " + String.join(";", row));
                    continue;
                }

                String productName = row[1].trim();
                String categoryName = row[2].trim();
                String brandName = row[3].trim();
                BigDecimal packageQuantity = new BigDecimal(row[4].trim());
                String packageUnit = row[5].trim();
                BigDecimal priceValue = new BigDecimal(row[6].trim());
                String currency = row[7].trim();

                Category category = categoryRepository
                        .findByNameIgnoreCase(categoryName)
                        .orElseGet(() -> categoryRepository.save(new Category(null, categoryName)));

                Brand brand = brandRepository
                        .findByNameIgnoreCase(brandName)
                        .orElseGet(() -> brandRepository.save(new Brand(null, brandName)));

                Product product = productRepository
                        .findByNameAndPackageQuantityAndPackageUnitAndBrand(productName, packageQuantity, packageUnit, brand)
                        .orElseGet(() -> {
                            Product p = new Product();
                            p.setName(productName);
                            p.setCategory(category);
                            p.setBrand(brand);
                            p.setPackageQuantity(packageQuantity);
                            p.setPackageUnit(packageUnit);
                            return productRepository.save(p);
                        });

                boolean alreadyExists = priceRepository.existsByProductAndSupermarketAndDate(product, supermarket, fileDate);
                if (alreadyExists) {
                    System.out.println("ℹ️ Price already exists: " + product.getName() + " [" + fileDate + "]");
                    continue;
                }

                Price price = new Price();
                price.setProduct(product);
                price.setSupermarket(supermarket);
                price.setValue(priceValue);
                price.setCurrency(currency);
                price.setDate(fileDate);

                priceRepository.save(price);
            }
        }

        System.out.println("✔️ Import completed for: " + supermarketName + " [" + fileDate + "]");
    }
}
