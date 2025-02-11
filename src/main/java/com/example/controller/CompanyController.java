package com.example.controller;

import com.example.dto.CompanyDTO;
import com.example.model.Company;
import com.example.service.CompanyService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    // Endpoint để tạo công ty mới
    @PostMapping("/create")
    public ResponseEntity<?> createCompany(@RequestBody Company company, @RequestParam String accountId) {
        try {
            Company newCompany = companyService.saveCompany(company, accountId);
            return ResponseHandler.resBuilder("Tạo công ty thành công.", HttpStatus.CREATED, newCompany);
        } catch (IllegalArgumentException e) {
            return ResponseHandler.resBuilder(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        } catch (Exception ex) {
            return ResponseHandler.resBuilder("Có lỗi xảy ra khi tạo công ty.", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllCompany() {
        try {
            List<Company> listCompany = companyService.getAllCompanies();
            List<CompanyDTO> res = new ArrayList<>();

            // Loop through each company and convert to CompanyDTO
            for (Company company : listCompany) {
                res.add(CompanyDTO.CompanyMapper.toDTO(company));  // Add each DTO to the list
            }
            return ResponseHandler.resBuilder("Lấy thông tin tất cả công ty thành công.", HttpStatus.CREATED, res);
        } catch (Exception ex) {
            return ResponseHandler.resBuilder("Có lỗi xảy ra khi lấy thông tin công ty công ty.", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    // Endpoint để lấy công ty theo ID
    @GetMapping("/{companyId}")
    public ResponseEntity<?> getCompany(@PathVariable String companyId) {
        try {
            Company company = companyService.getCompanyById(companyId);
            return ResponseHandler.resBuilder("Lấy thông tin công ty thành công.", HttpStatus.OK, company);
        } catch (Exception ex) {
            return ResponseHandler.resBuilder("Có lỗi xảy ra khi lấy thông tin công ty.", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

//    // Endpoint để thêm một nghệ sĩ vào công ty
//    @PostMapping("/{companyId}/add-artist")
//    public ResponseEntity<?> addArtistToCompany(@PathVariable String companyId, @RequestBody Artist artist) {
//        try {
//            companyService.addArtistToCompany(companyId, artist);
//            return ResponseHandler.resBuilder("Thêm nghệ sĩ vào công ty thành công.", HttpStatus.CREATED, null);
//        } catch (Exception ex) {
//            return ResponseHandler.resBuilder("Có lỗi xảy ra khi thêm nghệ sĩ vào công ty.", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
//        }
//    }
    // Endpoint để lấy tất cả nghệ sĩ của công ty
   
}
