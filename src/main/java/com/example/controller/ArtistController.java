package com.example.controller;

import com.example.dto.ArtistDTO;
import com.example.model.Artist;
import com.example.service.ArtistService;
import com.example.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    @Autowired
    private ArtistService artistService;
    @Autowired
    private CompanyService companyService;

    // Endpoint để tạo mới một nghệ sĩ
    @PostMapping("/create")
    public ResponseEntity<?> createArtist(@RequestBody Artist artist, @RequestParam String accountId, @RequestParam String companyId) {
        try {
            // Validate accountId
            if (accountId == null || accountId.isEmpty()) {
                return ResponseHandler.resBuilder("Yêu cầu id của tài khoản.", HttpStatus.BAD_REQUEST, null);
            }

            // Validate companyId
            if (companyId == null || companyId.isEmpty()) {
                return ResponseHandler.resBuilder("Yêu cầu id của công ty.", HttpStatus.BAD_REQUEST, null);
            }

            // Save the new artist
            Artist newArtist = companyService.addArtistToCompany(artist, accountId, companyId);
            return ResponseHandler.resBuilder("Tạo thông tin nghệ sĩ thành công.", HttpStatus.CREATED, ArtistDTO.ArtistMapper.toDTO(artist));
        } catch (IllegalArgumentException e) {
            return ResponseHandler.resBuilder("Đầu vào không hợp lệ: " + e.getMessage(), HttpStatus.BAD_REQUEST, null);
        } catch (Exception e) {
            return ResponseHandler.resBuilder("An error occurred while creating the artist.", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // Endpoint để thêm nghệ sĩ vào công ty
    @PostMapping("/add-to-company")
    public ResponseEntity<?> addArtistToCompany(@RequestParam String companyId, @RequestBody Artist artist) {
        try {
            // Validate companyId
            if (companyId == null || companyId.isEmpty()) {
                return ResponseHandler.resBuilder("Company ID is required.", HttpStatus.BAD_REQUEST, null);
            }

            // Thêm nghệ sĩ vào công ty
            artistService.addArtistToCompany(companyId, artist);
            return ResponseHandler.resBuilder("Nghệ sĩ đã được thêm vào công ty thành công.", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.resBuilder("Có lỗi xảy ra khi thêm nghệ sĩ vào công ty.", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // Endpoint để lấy danh sách tất cả nghệ sĩ của một công ty
    @GetMapping("/company/{companyId}")
    public ResponseEntity<?> getAllArtistsByCompany(@PathVariable String companyId) {
        try {
            // Lấy danh sách nghệ sĩ của công ty
            List<Artist> artists = artistService.getAllArtistsByCompanyId(companyId);
            return ResponseHandler.resBuilder("Lấy danh sách nghệ sĩ của công ty thành công.", HttpStatus.OK, artists);
        } catch (Exception e) {
            return ResponseHandler.resBuilder("Có lỗi xảy ra khi lấy danh sách nghệ sĩ.", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // Endpoint để lấy thông tin nghệ sĩ theo ID
    @GetMapping("/{accountId}")
    public ResponseEntity<?> getArtistById(@PathVariable String accountId) {
        try {
            // Lấy thông tin nghệ sĩ theo ID
            Artist artist = artistService.findArtistById(accountId);
            return ResponseHandler.resBuilder("Lấy thông tin nghệ sĩ thành công.", HttpStatus.OK, artist);
        } catch (Exception e) {
            return ResponseHandler.resBuilder("Có lỗi xảy ra khi lấy thông tin nghệ sĩ.", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
