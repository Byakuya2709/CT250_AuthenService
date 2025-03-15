/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.service;

import com.example.controller.ResponseHandler;
import com.example.dto.OtpGenerate;
import com.example.dto.TicketResponse;
import com.example.exception.AccountBlockedException;
import com.example.exception.AccountNotFoundEx;
import com.example.exception.EmailAlreadyExistsException;
import com.example.exception.OtpGenerationException;
import com.example.exception.UserNotFoundException;
import com.example.model.Account;
import com.example.model.Company;
import com.example.model.Otp;
import com.example.model.User;
import com.example.repository.AccountRepository;
import com.example.repository.CompanyRepository;
import com.example.repository.UserRepository;
import com.example.request.NewAccountRequest;
import com.example.request.ResetPasswordRequest;
import com.example.request.VerificationRequest;

import java.util.*;

import javax.mail.AuthenticationFailedException;

import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author admin
 */
@Service
public class AccountService {
// Constants

    private static final int OTP_VALIDITY_MINUTES = 10;
    private static final String ERROR_EMAIL_EMPTY = "Email không được để trống";
    private static final String ERROR_OTP_INVALID = "Mã OTP không hợp lệ hoặc đã hết hạn";
    private static final String ERROR_EMAIL_EXISTS = "Email này đã tồn tại!";

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;




    @Scheduled(cron = "0 0 0 1 * ?") // Chạy vào 00:00 ngày 1 mỗi tháng
    public void deactivateExpiredAccounts() {
        List<Account> expiredAccounts = accountRepository.findByExpiredDayBefore(new Date());
        for (Account account : expiredAccounts) {
            account.setStatus(Account.AccountStatus.INACTIVE); // Chặn tài khoản
            accountRepository.save(account);
        }
    }


    public String encodePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public boolean verifyPassword(String password, String storedHash) {
        return BCrypt.checkpw(password, storedHash);
    }

    @Transactional
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }


    public Account createAccount(NewAccountRequest req) {
        if (accountRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email đã được sử dụng.");
        }

        // Kiểm tra mật khẩu có rỗng không
        if (req.getPassword() == null || req.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống.");
        }

        // Encode mật khẩu
        String encodedPassword = this.encodePassword(req.getPassword());

        // Xử lý Role hợp lệ
        Account.Type accountType;
        try {
            accountType = Account.Type.valueOf(req.getRole());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Vai trò không hợp lệ: " + req.getRole());
        }

        // Tạo tài khoản mới
        Account newAcc = new Account();
        newAcc.setEmail(req.getEmail());
        newAcc.setType(accountType);
        newAcc.setStatus(Account.AccountStatus.ACTIVE);
        newAcc.setPassword(encodedPassword);

        // Lưu vào database
        return accountRepository.save(newAcc);
    }


    public Page<Account> getAllAccountsWithPageable(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    @Transactional
    public void createAdminAccount() {
        String email = "admin@gmail.com";

        // Nếu chưa tồn tại admin thì tạo mới
        if (!this.existsByEmail(email)) {
            Account admin = new Account();
            admin.setEmail(email);
            admin.setPassword(this.encodePassword("admin123")); // Cần đổi thành mật khẩu mạnh hơn khi deploy
            admin.setType(Account.Type.ADMIN);  // Không cần valueOf nếu enum có sẵn
            admin.setStatus(Account.AccountStatus.ACTIVE);
            accountRepository.save(admin);
            System.out.println("Tài khoản admin đã được tạo.");
        } else {
            System.out.println("Tài khoản admin đã tồn tại.");
        }
    }

    public Account getAccountById(String id) {
        Optional<Account> getAcc = accountRepository.findById(id);
        if (getAcc.isPresent()) {
            return getAcc.get();
        } else {
            return null;
        }
    }

    //
    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    public Optional<Account> findAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public Optional<Account> findAccountById(String id) {
        return accountRepository.findById(id);
    }

    public void deleteAccount(String id) {
        accountRepository.deleteById(id);
    }

    public String generateCode(String email, String type) {
        if (email.isEmpty() || type.isEmpty()) {
            throw new IllegalArgumentException(ERROR_EMAIL_EMPTY);
        }

        if (existsByEmail(email)) {
            throw new EmailAlreadyExistsException(ERROR_EMAIL_EXISTS);
        }

        List<Otp> existingOtps = otpService.findAllByEmail(email);
        if (!existingOtps.isEmpty()) {
            otpService.deleteAllByEmail(email);
        }

        String otp = OtpGenerate.generateOtp();
        try {
            otpService.saveOtp(email, otp, type, OTP_VALIDITY_MINUTES);
            emailService.sendVerificationEmail(email, otp);
        } catch (Exception e) {
            throw new OtpGenerationException("Có lỗi xảy ra khi lưu mã OTP hoặc gửi email.");
        }

        return "Mã OTP đã được gửi đến email của bạn";
    }

    public String generateCode2(String email, String type) {
        if (email.isEmpty() || type.isEmpty()) {
            throw new IllegalArgumentException(ERROR_EMAIL_EMPTY);
        }

        if (!existsByEmail(email)) {
            throw new AccountNotFoundEx();
        }

        List<Otp> existingOtps = otpService.findAllByEmail(email);
        if (!existingOtps.isEmpty()) {
            otpService.deleteAllByEmail(email);
        }

        String otp = OtpGenerate.generateOtp();
        try {
            otpService.saveOtp(email, otp, type, OTP_VALIDITY_MINUTES);
            emailService.sendVerificationEmail(email, otp);
        } catch (Exception e) {
            throw new OtpGenerationException("Có lỗi xảy ra khi lưu mã OTP hoặc gửi email.");
        }

        return "Mã OTP đã được gửi đến email của bạn";
    }

    public String verifyOTP(VerificationRequest verificationReq) {
        String email = verificationReq.getEmail().trim();
        String otp = verificationReq.getCode().trim();
        String type = verificationReq.getType().trim();

        if (email.isEmpty() || otp.isEmpty() || type.isEmpty()) {
            throw new IllegalArgumentException(ERROR_EMAIL_EMPTY);
        }

        try {
            boolean isOtpValid = otpService.verifyOtp(email, otp, type);
            if (isOtpValid) {
                otpService.deleteOtp(email);

                return "Xác thực OTP thành công";
            } else {

                throw new OtpGenerationException(ERROR_OTP_INVALID);
            }
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi không xác định xảy ra khi xác thực mã OTP.");
        }
    }

    public String verifyOTP2(ResetPasswordRequest verificationReq) {
        String email = verificationReq.getEmail().trim();
        String otp = verificationReq.getCode().trim();
        String type = verificationReq.getType().trim();

        if (email.isEmpty() || otp.isEmpty() || type.isEmpty()) {
            throw new IllegalArgumentException(ERROR_EMAIL_EMPTY);
        }

        try {
            boolean isOtpValid = otpService.verifyOtp(email, otp, type);
            if (isOtpValid) {
                otpService.deleteOtp(email);

                return "Xác thực OTP thành công";
            } else {

                throw new OtpGenerationException(ERROR_OTP_INVALID);
            }
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi không xác định xảy ra khi xác thực mã OTP.");
        }
    }

    public Account authenticate(String email, String password) throws AuthenticationFailedException {
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> {
            return new UserNotFoundException("Tài khoản không tồn tại");
        });
        if (account.getStatus() == Account.AccountStatus.INACTIVE) {
            throw new AccountBlockedException("Tài khoản đã bị khóa");
        }
        if (verifyPassword(password, account.getPassword())) {
            return account;
        } else {
            throw new AuthenticationFailedException("Xác thực thất bại");
        }
    }

    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Tài khoản không tồn tại: " + email));
    }


    public Account blockAccount(String email) {
        // Fetch the account by email or throw an exception if not found
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Tài khoản không tồn tại"));

        // Update the account status to INACTIVE
        account.setStatus(Account.AccountStatus.INACTIVE);

        // Save and return the updated account
        return accountRepository.save(account);
    }

    public void DeleteAccount(String accountId) {

        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isEmpty()) {
            throw new UserNotFoundException("User không tồn tại với accountId: " + accountId);
        }

        Account account = optionalAccount.get();

        if (account.getType().toString().equals("USER")) {
            ObjectId objectId = new ObjectId(accountId);
            Optional<User> optionalUser = userRepository.findByAccountId(objectId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setAccountId(null); // Bỏ liên kết với Account
                userRepository.save(user); // Lưu lại thay đổi
            }
        } else {
            ObjectId objectId = new ObjectId(accountId);
            Optional<Company> optionalCompany = companyRepository.findByAccountId(objectId);

            if (optionalCompany.isPresent()) {
                Company company = optionalCompany.get();
                company.setAccountId(null); // Bỏ liên kết với Account
                companyRepository.save(company); // Lưu lại thay đổi
            }
        }

        // Cuối cùng, xóa tài khoản
        accountRepository.deleteById(accountId);
    }


    public Account blockAccountById(String accountId) {
        // Fetch the account by email or throw an exception if not found
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new UserNotFoundException("Tài khoản không tồn tại"));

        // Update the account status to INACTIVE
        account.setStatus(Account.AccountStatus.INACTIVE);

        // Save and return the updated account
        return accountRepository.save(account);
    }

    public Account UnblockAccountById(String accountId) {
        // Fetch the account by email or throw an exception if not found
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new UserNotFoundException("Tài khoản không tồn tại"));

        // Update the account status to INACTIVE
        account.setStatus(Account.AccountStatus.ACTIVE);

        // Save and return the updated account
        return accountRepository.save(account);
    }

    public boolean updatePassword(String email, String newPassword) {
        // Fetch the account by email or throw an exception if not found
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Tài khoản không tồn tại"));
        try {
            // Mã hóa mật khẩu mới trước khi lưu
            String encodedPassword = this.encodePassword(newPassword);
            account.setPassword(encodedPassword);
            account.setStatus(Account.AccountStatus.ACTIVE);
            // Lưu tài khoản đã cập nhật với mật khẩu mới
            accountRepository.save(account);

            return true; // Trả về true nếu cập nhật thành công
        } catch (Exception e) {

            return false; // Trả về false nếu có lỗi xảy ra
        }
    }

    public Map<String, Long> reportForAccount() {
        Map<String, Long> res = new HashMap<>();

        Long accountHasBlocked = accountRepository.countInactiveAccounts(); // Đếm tài khoản bị khóa
        Long account = accountRepository.countNonAdminAccounts(); // Đếm tài khoản không phải ADMIN

        res.put("countedUser", account);
        res.put("countedBlock", accountHasBlocked);

        return res;
    }

}
