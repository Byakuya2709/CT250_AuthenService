package com.example.controller;

import com.example.exception.AccountBlockedException;
import com.example.exception.UserNotFoundException;
import com.example.model.Account;
import com.example.request.EmailReq;
import com.example.request.LoginRequest;
import com.example.request.NewAccountRequest;
import com.example.service.AccountService;
import com.example.service.CompanyService;
import com.example.service.EmailService;
import com.example.service.UserService;
import com.example.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.AuthenticationFailedException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @PatchMapping("/blocked/account/{accountId}")
    public ResponseEntity<?> blockUserAccountTemporary(@PathVariable("accountId") String accountId) {
        try {
            // Fetch account and block it using the service
            Account account = accountService.blockAccountById(accountId);
            emailService.sendNotificationForBlockedEmail(account.getEmail().trim(),"http://localhost:3002/company/reset-password");
            // Return a success response
            return ResponseHandler.resBuilder("Đã tạm thời khóa tài khoản", HttpStatus.OK, account);


        } catch (UserNotFoundException ex) {
            // Handle specific exceptions like UserNotFoundException
            return ResponseHandler.resBuilder("Tài khoản không tồn tại", HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (Exception ex) {
            // Handle generic exceptions
            return ResponseHandler.resBuilder("Có lỗi xảy ra trong hệ thống", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @PostMapping("/accounts")
    public ResponseEntity<?> createNewAccount(@RequestBody NewAccountRequest req) {
        try {
            Account account = accountService.createAccount(req);
            return ResponseHandler.resBuilder("Tạo tài khoản mới thành công", HttpStatus.OK, account);
        } catch (IllegalArgumentException e) {
            // Nếu request không hợp lệ (email trùng, role sai, mật khẩu trống,...)
            return ResponseHandler.resBuilder(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        } catch (Exception e) {
            // Nếu có lỗi hệ thống khác
            return ResponseHandler.resBuilder("Đã xảy ra lỗi khi tạo tài khoản", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }


    @PatchMapping("/unblocked/account/{accountId}")
    public ResponseEntity<?> UnblockUserAccount(@PathVariable("accountId") String accountId) {
        try {
            // Fetch account and block it using the service
            Account account = accountService.UnblockAccountById(accountId);

            // Return a success response
            return ResponseHandler.resBuilder("Đã mở khóa tài khoản", HttpStatus.OK, account);
        } catch (UserNotFoundException ex) {
            // Handle specific exceptions like UserNotFoundException
            return ResponseHandler.resBuilder("Tài khoản không tồn tại", HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (Exception ex) {
            // Handle generic exceptions
            return ResponseHandler.resBuilder("Có lỗi xảy ra trong hệ thống", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @GetMapping("/accounts")
    public ResponseEntity<?> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            if (size > 100) {
                size = 100; // Cap size at 100 to prevent large queries
            }
            Pageable pageable = PageRequest.of(page, size);
            return ResponseHandler.resBuilder("Danh sách tài khoản", HttpStatus.OK, accountService.getAllAccountsWithPageable(pageable));
        } catch (Exception ex) {
            // Handle generic exceptions
            return ResponseHandler.resBuilder("Có lỗi xảy ra trong hệ thống", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @GetMapping("/accounts/profiles/company")
    public ResponseEntity<?> getAllCompanyProfiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            if (size > 100) {
                size = 100; // Cap size at 100 to prevent large queries
            }
            Pageable pageable = PageRequest.of(page, size);
            return ResponseHandler.resBuilder("Danh sách công ty", HttpStatus.OK, companyService.getAllCompanyWithPageable(pageable));
        } catch (Exception ex) {
            // Handle generic exceptions
            return ResponseHandler.resBuilder("Có lỗi xảy ra trong hệ thống", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @GetMapping("/accounts/profiles/user")
    public ResponseEntity<?> getAllUserProfiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            if (size > 100) {
                size = 100; // Cap size at 100 to prevent large queries
            }
            Pageable pageable = PageRequest.of(page, size);
            return ResponseHandler.resBuilder("Danh sách người dùng", HttpStatus.OK, userService.getAllUserWithPageable(pageable));
        } catch (Exception ex) {
            // Handle generic exceptions
            return ResponseHandler.resBuilder("Có lỗi xảy ra trong hệ thống", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @DeleteMapping("/account/{accountId}")
    public ResponseEntity<?> DeleteAccount(@PathVariable("accountId") String accountId) {
        try {
            accountService.DeleteAccount(accountId);
            return ResponseHandler.resBuilder("Xóa tài khoản thành công", HttpStatus.OK, null);
        } catch (UserNotFoundException ex) {
            return ResponseHandler.resBuilder("Tài khoản không tồn tại", HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (Exception ex) {
            return ResponseHandler.resBuilder("Có lỗi xảy ra trong hệ thống", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @GetMapping("/account/report")
    public ResponseEntity<?> reportForAccount() {
        try {
            return ResponseHandler.resBuilder("Lấy báo cáo thành công", HttpStatus.OK, accountService.reportForAccount());
        } catch (Exception ex) {
            return ResponseHandler.resBuilder("Có lỗi xảy ra trong hệ thống", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
}
