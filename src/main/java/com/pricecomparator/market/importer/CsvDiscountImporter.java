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
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CsvDiscountImporter {

    private final ProductRepository productRepository;
    private final SupermarketRepository supermarketRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final DiscountRepository discountRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void importFile(String filePath, String supermarketName) throws IOException, CsvValidationException {

        Supermarket supermarket = supermarketRepository
                .findByNameIgnoreCase(supermarketName)
                .orElseGet(() -> supermarketRepository.save(new Supermarket(null, supermarketName)));

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build())  {
            String[] row;
            boolean skipHeader = true;

            while ((row = reader.readNext()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }


                String productName = row[1].trim();
                String brandName = row[2].trim();
                BigDecimal quantity = new BigDecimal(row[3].trim());
                String unit = row[4].trim();
                String categoryName = row[5].trim();
                LocalDate fromDate = LocalDate.parse(row[6].trim(), formatter);
                LocalDate toDate = LocalDate.parse(row[7].trim(), formatter);
                Integer discountPercent = Integer.parseInt(row[8].trim());


                Category category = categoryRepository
                        .findByNameIgnoreCase(categoryName)
                        .orElseGet(() -> categoryRepository.save(new Category(null, categoryName)));


                Brand brand = brandRepository
                        .findByNameIgnoreCase(brandName)
                        .orElseGet(() -> brandRepository.save(new Brand(null, brandName)));


                Product product = productRepository
                        .findByNameAndPackageQuantityAndPackageUnitAndBrand(productName, quantity, unit, brand)
                        .orElseGet(() -> {
                            Product p = new Product();
                            p.setName(productName);
                            p.setCategory(category);
                            p.setBrand(brand);
                            p.setPackageQuantity(quantity);
                            p.setPackageUnit(unit);
                            return productRepository.save(p);
                        });

                // Create and save discount
                Discount discount = new Discount();
                discount.setProduct(product);
                discount.setSupermarket(supermarket);
                discount.setFromDate(fromDate);
                discount.setToDate(toDate);
                discount.setPercentageOfDiscount(discountPercent);

                discountRepository.save(discount);
            }
        }

        System.out.println("✔️ Discount import completed for: " + supermarketName);
    }


    public void importAllFromFolder(String folderPath) throws IOException, CsvValidationException {
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("❌ Invalid folder: " + folderPath);
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));
        if (files == null) return;

        for (File file : files) {
            try (CSVReader reader = new CSVReader(new FileReader(file))) {
                String[] header = reader.readNext();
                if (header == null) continue;

                boolean isDiscountFile = false;
                for (String column : header) {
                    if (column.toLowerCase().contains("percentage_of_discount")) {
                        isDiscountFile = true;
                        break;
                    }
                }

                if (!isDiscountFile) {
                    System.out.println("❌ File ignored (not a discount file): " + file.getName());
                    continue;
                }


                String fileName = file.getName();
                Matcher matcher = Pattern.compile("(lidl|kaufland|profi).*?(\\d{4}-\\d{2}-\\d{2})").matcher(fileName.toLowerCase());

                if (matcher.find()) {
                    String supermarket = capitalize(matcher.group(1));
                    importFile(file.getAbsolutePath(), supermarket);
                } else {
                    System.out.println("❌ Could not extract supermarket or date from: " + fileName);
                }

            } catch (Exception e) {
                System.err.println("❌ Error processing file " + file.getName() + ": " + e.getMessage());
            }
        }
    }
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}

