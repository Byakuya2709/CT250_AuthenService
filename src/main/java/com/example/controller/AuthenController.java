/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import com.example.exception.AccountBlockedException;
import com.example.exception.AccountNotFoundEx;
import com.example.exception.EmailAlreadyExistsException;
import com.example.exception.OtpGenerationException;
import com.example.exception.UserNotFoundException;
import com.example.model.Account;
import com.example.model.Company;
import com.example.request.EmailReq;
import com.example.request.LoginRequest;
import com.example.request.NewPasswordRequest;
import com.example.request.ResetPasswordRequest;
import com.example.request.VerificationRequest;
import com.example.service.AccountService;
import com.example.utils.JwtUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.mail.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author admin
 */
@RestController
@RequestMapping("/auth")
public class AuthenController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/generate")
    public ResponseEntity<?> generateVerificationCode(@RequestBody EmailReq req) {
        try {
            // Validate input
            if (req.getEmail() == null || req.getEmail().trim().isEmpty()) {
                return ResponseHandler.resBuilder("Email không được để trống", HttpStatus.BAD_REQUEST, null);
            }

            // Gửi mã OTP đến email
            String message = accountService.generateCode(req.getEmail().trim(), "Registration");

            return ResponseHandler.resBuilder(message, HttpStatus.CREATED, null);
        } catch (IllegalArgumentException ex) {
            return ResponseHandler.resBuilder(ex.getMessage(), HttpStatus.CONFLICT, null); // 409 Conflict
        } catch (EmailAlreadyExistsException ex) {
            return ResponseHandler.resBuilder(ex.getMessage(), HttpStatus.CONFLICT, null); // 409 Conflict
        } catch (OtpGenerationException ex) {
            return ResponseHandler.resBuilder(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null); // 500 Internal Server Error
        } catch (RuntimeException ex) {
            return ResponseHandler.resBuilder(ex.getMessage(), HttpStatus.BAD_REQUEST, null); // 400 Bad Request
        } catch (Exception e) {
            return ResponseHandler.resBuilder("Có lỗi không xác định xảy ra: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping("/generate-passcode")
    public ResponseEntity<?> generateVerificationPasswordCode(@RequestBody EmailReq req) {
        try {
            // Validate input
            if (req.getEmail() == null || req.getEmail().trim().isEmpty()) {
                return ResponseHandler.resBuilder("Email không được để trống", HttpStatus.BAD_REQUEST, null);
            }
            // Gửi mã OTP đến email
            String message = accountService.generateCode2(req.getEmail().trim(), "ResetPassword");

            return ResponseHandler.resBuilder(message, HttpStatus.CREATED, null);
        } catch (IllegalArgumentException ex) {
            return ResponseHandler.resBuilder(ex.getMessage(), HttpStatus.CONFLICT, null); // 409 Conflict
        } catch (AccountNotFoundEx ex) {
            return ResponseHandler.resBuilder(ex.getMessage(), HttpStatus.CONFLICT, null); // 409 Conflict
        } catch (OtpGenerationException ex) {
            return ResponseHandler.resBuilder(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null); // 500 Internal Server Error
        } catch (RuntimeException ex) {
            return ResponseHandler.resBuilder(ex.getMessage(), HttpStatus.BAD_REQUEST, null); // 400 Bad Request
        } catch (Exception e) {
            return ResponseHandler.resBuilder("Có lỗi không xác định xảy ra: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAccount(@RequestBody EmailReq email) {
        try {
            Optional<Account> listCompany = accountService.findAccountByEmail(email.getEmail());
            return ResponseHandler.resBuilder("Lấy thông tin tất cả tài khoản thành công.", HttpStatus.CREATED, listCompany.get());
        } catch (Exception ex) {
            return ResponseHandler.resBuilder("Có lỗi xảy ra khi lấy thông tin tài khoản.", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
@PostMapping("/account/resetpassword")
    public ResponseEntity<?> resetPass(@RequestBody NewPasswordRequest req) {
        String email = req.getEmail().trim();
        String newPassword = req.getNewPassword();
        if (email == null || email.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            return ResponseHandler.resBuilder("Email và mật khẩu không được để trống", HttpStatus.BAD_REQUEST, null);
        }

        boolean isUpdated = accountService.updatePassword(email, newPassword);
        if (isUpdated) {
            return ResponseHandler.resBuilder("Mật khẩu đã được thay đổi thành công", HttpStatus.OK, null);
        } else {
            return ResponseHandler.resBuilder("Có lỗi xảy ra khi cập nhật mật khẩu, vui lòng thử lại", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }

    }
    @PostMapping("/account/verify")
    public ResponseEntity<?> verifyOTP(@RequestBody ResetPasswordRequest verificationReq) {
        String email = verificationReq.getEmail();
        String otp = verificationReq.getCode();
        String type = verificationReq.getType();
        System.out.println(type);

        try {
            String otpVerificationResult = accountService.verifyOTP2(verificationReq);
            if (!otpVerificationResult.equals("Xác thực OTP thành công")) {
                return ResponseHandler.resBuilder(otpVerificationResult, HttpStatus.BAD_REQUEST, null); // OTP verification failed
            }
            return ResponseHandler.resBuilder(otpVerificationResult, HttpStatus.OK, null); // OTP verification failed
        } catch (RuntimeException ex) {
            return ResponseHandler.resBuilder(ex.getMessage(), HttpStatus.BAD_REQUEST, null);
        } catch (Exception ex) {
            return ResponseHandler.resBuilder("Có lỗi xảy ra khi đăng ký tài khoản.", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody VerificationRequest req) {
        // Validate inputs
        if (req.getCode() == null) {
            return ResponseHandler.resBuilder("Mã không được để trống", HttpStatus.BAD_REQUEST, null);
        }
        // Verify OTP
        try {
            String otpVerificationResult = accountService.verifyOTP(req);
            if (!otpVerificationResult.equals("Xác thực OTP thành công")) {
                return ResponseHandler.resBuilder(otpVerificationResult, HttpStatus.BAD_REQUEST, null); // OTP verification failed
            }

//             If OTP is valid, proceed with account creation
            String encodedPassword = accountService.encodePassword(req.getPassword());
            Account account = new Account(req.getEmail().trim(), encodedPassword, Account.Type.valueOf(req.getRole()));
            account.setStatus(Account.AccountStatus.ACTIVE);
            Account createdAccount = accountService.saveAccount(account);
            return ResponseHandler.resBuilder("Tạo tài khoản thành công", HttpStatus.CREATED, createdAccount);

        } catch (RuntimeException ex) {
            return ResponseHandler.resBuilder(ex.getMessage(), HttpStatus.BAD_REQUEST, null);
        } catch (Exception ex) {
            return ResponseHandler.resBuilder("Có lỗi xảy ra khi đăng ký tài khoản.", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
    @PostMapping("/admin/login")
    public ResponseEntity<?> AdminLogin(@RequestBody LoginRequest req) {
        String email = req.getEmail() != null ? req.getEmail().trim() : "";
        String password = req.getPassword();

        if (email.isEmpty() || password.isEmpty()) {
            return ResponseHandler.resBuilder("Email và mật khẩu không được để trống", HttpStatus.BAD_REQUEST, null);
        }

        try {
            Account account = accountService.authenticate(email, password);
            if (account != null) {

                if (account.getType() == Account.Type.ADMIN) {
                    String token = jwtUtil.generateToken(account);
                    Map<String, Object> response = new HashMap<>();
                    response.put("token", token);
                    response.put("userId", account.getId());
                    response.put("role", account.getType().toString());

                    return ResponseHandler.resBuilder("Đăng nhập thành công", HttpStatus.OK, response);
                } else return ResponseHandler.resBuilder("Đây không phải tài khoản ADMIN", HttpStatus.FORBIDDEN, null);
            } else {
                return ResponseHandler.resBuilder("Thông tin đăng nhập không chính xác", HttpStatus.UNAUTHORIZED, null);
            }
        } catch (UserNotFoundException e) {
            return ResponseHandler.resBuilder(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        } catch (AuthenticationFailedException ex) {
            return ResponseHandler.resBuilder(ex.getMessage(), HttpStatus.UNAUTHORIZED, ex.getMessage());
        } catch (AccountBlockedException ex) {
            return ResponseHandler.resBuilder(ex.getMessage(), HttpStatus.FORBIDDEN, ex.getMessage());
        } catch (Exception ex) {
            return ResponseHandler.resBuilder("Có lỗi xảy ra trong quá trình đăng nhập", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
    @PostMapping("/company/login")
    public ResponseEntity<?> CompanyLogin(@RequestBody LoginRequest req) {
        String email = req.getEmail() != null ? req.getEmail().trim() : "";
        String password = req.getPassword();

        if (email.isEmpty() || password.isEmpty()) {
            return ResponseHandler.resBuilder("Email và mật khẩu không được để trống", HttpStatus.BAD_REQUEST, null);
        }

        try {
            Account account = accountService.authenticate(email, password);
            if (account != null) {
                if (account.getType() == Account.Type.COMPANY) {
                String token = jwtUtil.generateToken(account);
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("userId", account.getId());
                response.put("role", account.getType().toString());

                return ResponseHandler.resBuilder("Đăng nhập thành công", HttpStatus.OK, response);}
                else return ResponseHandler.resBuilder("Đây không phải tài khoản Công ty", HttpStatus.FORBIDDEN, null);
            } else {
                return ResponseHandler.resBuilder("Thông tin đăng nhập không chính xác", HttpStatus.UNAUTHORIZED, null);
            }
        } catch (UserNotFoundException e) {
            return ResponseHandler.resBuilder(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        } catch (AuthenticationFailedException ex) {
            return ResponseHandler.resBuilder(ex.getMessage(), HttpStatus.UNAUTHORIZED, ex.getMessage());
        } catch (AccountBlockedException ex) {
            return ResponseHandler.resBuilder(ex.getMessage(), HttpStatus.FORBIDDEN, ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseHandler.resBuilder("Có lỗi xảy ra trong quá trình đăng nhập", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> UserLogin(@RequestBody LoginRequest req) {
        String email = req.getEmail() != null ? req.getEmail().trim() : "";
        String password = req.getPassword();

        if (email.isEmpty() || password.isEmpty()) {
            return ResponseHandler.resBuilder("Email và mật khẩu không được để trống", HttpStatus.BAD_REQUEST, null);
        }

        try {
            Account account = accountService.authenticate(email, password);
            if (account != null) {

                if (account.getType() == Account.Type.USER) {
                    String token = jwtUtil.generateToken(account);
                    Map<String, Object> response = new HashMap<>();
                    response.put("token", token);
                    response.put("userId", account.getId());
                    response.put("role", account.getType().toString());

                    return ResponseHandler.resBuilder("Đăng nhập thành công", HttpStatus.OK, response);}
                else return ResponseHandler.resBuilder("Đây không phải tài khoản Công ty", HttpStatus.FORBIDDEN, null);
            } else {
                return ResponseHandler.resBuilder("Thông tin đăng nhập không chính xác", HttpStatus.UNAUTHORIZED, null);
            }
        } catch (UserNotFoundException e) {
            return ResponseHandler.resBuilder(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        } catch (AuthenticationFailedException ex) {
            return ResponseHandler.resBuilder(ex.getMessage(), HttpStatus.UNAUTHORIZED, ex.getMessage());
        } catch (AccountBlockedException ex) {
            return ResponseHandler.resBuilder(ex.getMessage(), HttpStatus.FORBIDDEN, ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseHandler.resBuilder("Có lỗi xảy ra trong quá trình đăng nhập", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<?> getAccount(@PathVariable("accountId") String accountId) {
        try {
            // Lấy thông tin tài khoản từ service
            Account account = accountService.getAccountById(accountId);

            // Nếu không tìm thấy tài khoản
            if (account == null) {
                return ResponseHandler.resBuilder("Không tìm thấy tài khoản", HttpStatus.NOT_FOUND, null);
            }

            // Trả về thông tin tài khoản
            return ResponseHandler.resBuilder("Lấy thông tin tài khoản thành công", HttpStatus.OK, account);
        } catch (Exception ex) {
            // Trường hợp lỗi hệ thống
            return ResponseHandler.resBuilder("Có lỗi xảy ra trong hệ thống", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @PostMapping("/blocked")
    public ResponseEntity<?> blockAccountTemporary(@RequestBody EmailReq req) {
        try {
            // Fetch account and block it using the service
            Account account = accountService.blockAccount(req.getEmail());

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
}
