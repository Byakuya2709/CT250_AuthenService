package com.example.service;

import com.example.model.Artist;
import com.example.model.Company;
import com.example.repository.ArtistRepository;
import com.example.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private CompanyRepository companyRepository;

    // Lưu một nghệ sĩ mới
    public Artist saveArtist(Artist artist) {
        return artistRepository.save(artist);
    }

    // Lấy thông tin nghệ sĩ theo ID
    public Artist getArtistById(String artistId) {
        Optional<Artist> artist = artistRepository.findById(artistId);
        if (artist.isPresent()) {
            return artist.get();
        } else {
            throw new IllegalArgumentException("Nghệ sĩ không tồn tại.");
        }
    }

    // Thêm nghệ sĩ vào công ty
    @Transactional
    public void addArtistToCompany(String companyId, Artist artist) {
        // Lấy công ty theo ID
        Optional<Company> companyOpt = companyRepository.findById(companyId);
        if (!companyOpt.isPresent()) {
            throw new IllegalArgumentException("Công ty không tồn tại.");
        }

        Company company = companyOpt.get();

        // Thêm công ty vào nghệ sĩ
        artist.setCompany(company);

        // Lưu nghệ sĩ vào cơ sở dữ liệu
        artistRepository.save(artist);
    }

    // Lấy tất cả nghệ sĩ của một công ty
    public List<Artist> getAllArtistsByCompanyId(String companyId) {
        return artistRepository.findAllByCompanyId(companyId);
    }
}
