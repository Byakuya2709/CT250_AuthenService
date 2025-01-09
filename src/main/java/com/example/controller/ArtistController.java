package com.example.controller;

import com.example.model.Artist;
import com.example.service.ArtistService;
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

    // Endpoint để tạo mới một nghệ sĩ
    @PostMapping("/create")
    public ResponseEntity<?> createArtist(@RequestBody Artist artist) {
        try {
            // Lưu nghệ sĩ mới
            Artist newArtist = artistService.saveArtist(artist);
            return ResponseHandler.resBuilder("Nghệ sĩ đã được tạo thành công.", HttpStatus.CREATED, newArtist);
        } catch (Exception e) {
            return ResponseHandler.resBuilder("Có lỗi xảy ra khi tạo nghệ sĩ.", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // Endpoint để thêm nghệ sĩ vào công ty
    @PostMapping("/add-to-company")
    public ResponseEntity<?> addArtistToCompany(@RequestParam String companyId, @RequestBody Artist artist) {
        try {
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
    @GetMapping("/{artistId}")
    public ResponseEntity<?> getArtistById(@PathVariable String artistId) {
        try {
            // Lấy thông tin nghệ sĩ theo ID
            Artist artist = artistService.getArtistById(artistId);
            return ResponseHandler.resBuilder("Lấy thông tin nghệ sĩ thành công.", HttpStatus.OK, artist);
        } catch (Exception e) {
            return ResponseHandler.resBuilder("Có lỗi xảy ra khi lấy thông tin nghệ sĩ.", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
