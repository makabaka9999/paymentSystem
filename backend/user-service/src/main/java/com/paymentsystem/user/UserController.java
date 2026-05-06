package com.paymentsystem.user;

import com.paymentsystem.common.ApiResponse;
import com.paymentsystem.common.security.CurrentUser;
import com.paymentsystem.common.web.UserContext;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping("/profile")
    public ApiResponse<Map<String, Object>> profile(HttpServletRequest request) {
        CurrentUser current = UserContext.from(request);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("userId", current.getUserId());
        data.put("username", current.getUsername());
        data.put("role", current.getRole());
        return ApiResponse.ok(data);
    }

    @GetMapping("/addresses")
    public ApiResponse<List<AddressView>> addresses() {
        return ApiResponse.ok(Arrays.asList(new AddressView(1L, "张三", "13800000000", "上海市浦东新区示例路 1 号", true)));
    }

    public static class AddressView {
        private Long id;
        private String contactName;
        private String phone;
        private String detail;
        private boolean defaultAddress;
        public AddressView(Long id, String contactName, String phone, String detail, boolean defaultAddress) {
            this.id = id; this.contactName = contactName; this.phone = phone; this.detail = detail; this.defaultAddress = defaultAddress;
        }
        public Long getId() { return id; }
        public String getContactName() { return contactName; }
        public String getPhone() { return phone; }
        public String getDetail() { return detail; }
        public boolean isDefaultAddress() { return defaultAddress; }
    }
}
