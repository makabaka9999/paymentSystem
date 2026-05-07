package com.paymentsystem.order.dto;

import java.util.List;

/**
 * 创建订单请求参数。
 */
public class CreateOrderRequest {
    /** 订单商品明细列表。 */
    private List<OrderItem> items;
    /** 收货地址 ID。 */
    private String addressId;

    /** @return 订单商品明细列表 */
    public List<OrderItem> getItems() { return items; }

    /** @param items 订单商品明细列表 */
    public void setItems(List<OrderItem> items) { this.items = items; }

    /** @return 收货地址 ID */
    public String getAddressId() { return addressId; }

    /** @param addressId 收货地址 ID */
    public void setAddressId(String addressId) { this.addressId = addressId; }
}
