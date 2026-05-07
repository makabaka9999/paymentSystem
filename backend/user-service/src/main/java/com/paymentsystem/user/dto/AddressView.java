package com.paymentsystem.user.dto;

/**
 * 用户收货地址响应视图。
 */
public class AddressView {
    /** 地址 ID。 */
    private Long id;
    /** 联系人姓名。 */
    private String contactName;
    /** 联系人手机号。 */
    private String phone;
    /** 详细地址。 */
    private String detail;
    /** 是否默认地址。 */
    private boolean defaultAddress;

    /**
     * 创建收货地址视图。
     */
    public AddressView(Long id, String contactName, String phone, String detail, boolean defaultAddress) {
        this.id = id; this.contactName = contactName; this.phone = phone; this.detail = detail; this.defaultAddress = defaultAddress;
    }

    /** @return 地址 ID */
    public Long getId() { return id; }
    /** @return 联系人姓名 */
    public String getContactName() { return contactName; }
    /** @return 联系人手机号 */
    public String getPhone() { return phone; }
    /** @return 详细地址 */
    public String getDetail() { return detail; }
    /** @return 是否默认地址 */
    public boolean isDefaultAddress() { return defaultAddress; }
}
