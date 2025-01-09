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

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ArtistRepository artistRepository;

    // Thêm một nghệ sĩ vào công ty
    public void addArtistToCompany(String companyId, Artist artist) {
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new RuntimeException("Company not found"));
        artist.setCompany(company);
        artistRepository.save(artist);
        company.getArtists().add(artist);
        companyRepository.save(company);
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
    public List<Company> getAllCompanies(){
        return companyRepository.findAll();
    } 
    // Lưu nghệ sĩ
    public Artist saveArtist(Artist artist) {
        return artistRepository.save(artist);
    }
}
