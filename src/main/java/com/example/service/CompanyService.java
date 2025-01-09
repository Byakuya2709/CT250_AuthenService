package com.example.service;

import com.example.model.Account;
import com.example.model.Company;
import com.example.model.Artist;
import com.example.repository.AccountRepository;
import com.example.repository.CompanyRepository;
import com.example.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ArtistRepository artistRepository;

    // Thêm một nghệ sĩ vào công ty
    @Transactional
    public Artist addArtistToCompany(Artist artist, String accountId, String companyId) {
        // Check if the account exists
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty()) {
            throw new IllegalArgumentException("Tài khoản không hợp lệ"); // Invalid account
        }
        Account account = accountOptional.get();

        // Set the account for the artist
        artist.setAccount(account);

        // Check if the company exists
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        // Set the company for the artist
        artist.setCompany(company);

        // Save the artist to the database
        artistRepository.save(artist);

        // Add the artist to the company's list of artists
        company.getArtists().add(artist);

        // Save the company to persist the relationship
        companyRepository.save(company);

        // Return the saved artist
        return artist;
    }

    // Lấy tất cả nghệ sĩ của một công ty
    public List<Artist> getAllArtistsByCompanyId(String companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new RuntimeException("Company not found"));
        return company.getArtists();
    }

    // Lấy công ty theo ID
    public Company getCompanyById(String companyId) {
        return companyRepository.findById(companyId).orElseThrow(() -> new RuntimeException("Company not found"));
    }

    // Lưu công ty
    public Company saveCompany(Company company, String accountId) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty()) {
            throw new IllegalArgumentException("Tài khoản không hợp lệ");
        }
        Account account = accountOptional.get();
        company.setAccount(account);
        return companyRepository.save(company);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company findCompanyByAccountId(String accountId) {
        return companyRepository.findByAccount_Id(accountId)
                .orElseThrow(() -> new RuntimeException("No company found for account ID: " + accountId));
    }

    // Lưu nghệ sĩ
    public Artist saveArtist(Artist artist, String accountId) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty()) {
            throw new IllegalArgumentException("Tài khoản không hợp lệ");
        }
        Account account = accountOptional.get();
        artist.setAccount(account);

        // Find the company associated with the account
        Optional<Company> companyOptional = companyRepository.findByAccount_Id(accountId);
        if (companyOptional.isEmpty()) {
            throw new IllegalArgumentException("Công ty không tồn tại");
        }
        Company company = companyOptional.get(); // Fix: Get the company from Optional
        artist.setCompany(company);

        // Save the artist and update the company
        artistRepository.save(artist);
        company.getArtists().add(artist);
        companyRepository.save(company);

        return artist; // Return the saved artist
    }

}
