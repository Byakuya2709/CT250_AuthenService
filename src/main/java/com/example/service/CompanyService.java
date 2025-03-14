package com.example.service;

import com.example.dto.CompanyDTO;
import com.example.dto.CompanySummary;
import com.example.exception.CompanyNotFoundEx;
import com.example.model.Account;
import com.example.model.Company;
import com.example.repository.AccountRepository;
import com.example.repository.CompanyRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // Thêm một nghệ sĩ vào công t
    // Lấy công ty theo ID
    public Company getCompanyById(String companyId) {
        return companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFoundEx("Company not found"));
    }

    public Company getCompanyByAccountId(String accountId) {
        return companyRepository.findByAccountId(new ObjectId(accountId)).orElseThrow(() -> new CompanyNotFoundEx("Company not found"));
    }

    // Lưu công ty
    public Company saveCompany(Company company, String accountId) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty()) {
            throw new IllegalArgumentException("Tài khoản không hợp lệ");
        }
        company.setAccountId(new ObjectId(accountId));
        return companyRepository.save(company);
    }

    public Page<Company> getAllCompanyWithPageable(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    public Company updateCompany(CompanyDTO req) {
        Optional<Company> company = companyRepository.findById(req.getId());
        if (company.isEmpty()) {
            throw new CompanyNotFoundEx("Công ty không tồn tại!!!");
        }
        Company existingCompany = company.get();
        existingCompany.setCompanyName(req.getCompanyName());
        existingCompany.setCompanyMail(req.getCompanyMail());
        existingCompany.setCompanyPhone(req.getCompanyPhone());
        existingCompany.setCompanyAddress(req.getCompanyAddress());
        existingCompany.setLogoURL(req.getLogoURL());

        return companyRepository.save(existingCompany);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company findCompanyByAccountId(String accountId) {
        return companyRepository.findByAccountId(new ObjectId(accountId))
                .orElseThrow(() -> new RuntimeException("No company found for account ID: " + accountId));
    }

    public List<CompanySummary> getAllCompaniesWithIdAndName() {
        return companyRepository.findAllWithIdAndName();
    }

}
