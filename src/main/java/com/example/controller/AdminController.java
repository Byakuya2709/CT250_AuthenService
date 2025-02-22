package com.example.controller;


import com.example.exception.AccountBlockedException;
import com.example.exception.UserNotFoundException;
import com.example.model.Account;
import com.example.request.LoginRequest;
import com.example.service.AccountService;
import com.example.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.AuthenticationFailedException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admins")
public class AdminController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
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
}
