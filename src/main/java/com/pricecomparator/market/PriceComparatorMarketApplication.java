package com.pricecomparator.market;

import com.opencsv.exceptions.CsvValidationException;
import com.pricecomparator.market.importer.CsvDiscountImporter;
import com.pricecomparator.market.importer.CsvProductImporter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class PriceComparatorMarketApplication {

	@Bean
	CommandLineRunner run(CsvDiscountImporter discountImporter, CsvProductImporter productImporter) {
		return args -> {
			try {
				productImporter.importAllFromFolder("src/main/resources/csv/products");
			} catch (IOException | CsvValidationException e) {
				System.err.println("❌ Error while importing product files: " + e.getMessage());
			}

			try {
				discountImporter.importAllFromFolder("src/main/resources/csv/discounts");
			} catch (IOException | CsvValidationException e) {
				System.err.println("❌ Error while importing discount files: " + e.getMessage());
			}
		};
	}


	public static void main(String[] args) {
		SpringApplication.run(PriceComparatorMarketApplication.class, args);
	}
}
