package com.shopping.orderservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.orderservice.client.Wallet;
import com.shopping.orderservice.client.WalletRequest;
import com.shopping.orderservice.client.WalletServiceClient;
import com.shopping.orderservice.entity.Order;
import com.shopping.orderservice.entity.ShippingAddress;
import com.shopping.orderservice.service.OrderService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final WalletServiceClient walletServiceClient;

    @PostMapping(value = "/placeOrder", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.placeOrder(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Order>> getOrderById(@PathVariable String id){
        Optional<Order> order = orderService.findById(id);
        return ResponseEntity.ok(order);
    }
    @GetMapping("/order/{orderId}")
    public ResponseEntity< Optional<Order>> getOrderByOrderId(@PathVariable int OrderId){
        Optional<Order> order = orderService.getByOrderId(OrderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping(value = "/onlinePayment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> onlinePayment(@RequestBody Order order) {
        ResponseEntity<Wallet> byCustomerId = walletServiceClient.getByCustomerId(order.getCustomerId());
        Wallet body = byCustomerId.getBody();
        if (body.getWalletId() != 0) {
            WalletRequest walletRequest = WalletRequest
                    .builder()
                    .walletId(body.getWalletId())
                    .amount(order.getTotalPrice())
                    .transactionType("Withdraw")
                    .build();
            ResponseEntity<Void> payMoney = walletServiceClient.payMoney(walletRequest);
            if (payMoney.getStatusCode() == HttpStatus.OK) {
                changeOrderStatus("PAID", order.getOrderId());
            } else {
                changeOrderStatus("PAYMENT-ISSUE", order.getOrderId());
            }
        } else {
            changeOrderStatus("WALLET-NOT-CONFIGURED-FOR-CUSTOMER", order.getOrderId());
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.getOrderById(order.getOrderId()).get());
    }

    @GetMapping(value = "/changeStatus/{status}/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changeOrderStatus(@RequestParam (value = "status") String status,
                                                  @RequestParam (value = "orderId") int orderId) {
        orderService.changeOrderStatus(orderId, status);
        return ResponseEntity.ok("Order Status Updated.");
    }


    @GetMapping(value = "/allOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> getAllOrder() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping(value = "/findMaxByOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> getLatestOrder() {
        return ResponseEntity.ok(orderService.getLatestOrder());
    }


    @GetMapping(value = "/customer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> getByCustomerId(@PathVariable("customerId") String customerId ) {
        return ResponseEntity.ok(orderService.getOrderByCustomerId(customerId));
    }

    @DeleteMapping(value = "/{orderId}")
    public ResponseEntity<String> deleteOrder(@RequestParam(value = "orderId") int orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok("Order Deleted Successfully.");
    }
}
