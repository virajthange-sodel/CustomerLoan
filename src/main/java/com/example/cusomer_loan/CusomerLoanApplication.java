package com.example.cusomer_loan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

@SpringBootApplication
public class CusomerLoanApplication {

	public static void main(String[] args) {
		SpringApplication.run(CusomerLoanApplication.class, args);

//		symmetric algorith
//		we don't pass a raw AES key directly — instead you pass a password + salt, and Spring internally derives the actual AES key using:
		TextEncryptor textEncryptor = Encryptors.text("hjkfa89euh", "a3cb9fcd4f");   //Salt should contain only even number of hex decimal values

		String encrypted = textEncryptor.encrypt("Viraj");
		String decrypted = textEncryptor.decrypt(encrypted);
		System.out.println(encrypted);
		System.out.println("Decrypted string is "+ decrypted);
	}
}