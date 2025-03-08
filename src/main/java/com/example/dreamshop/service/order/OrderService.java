package com.example.dreamshop.service.order;

import com.example.dreamshop.dto.OrderDto;
import com.example.dreamshop.enums.OrderStatus;
import com.example.dreamshop.exceptions.ResourceNotFoundException;
import com.example.dreamshop.model.Cart;
import com.example.dreamshop.model.Order;
import com.example.dreamshop.model.OrderItem;
import com.example.dreamshop.model.Product;
import com.example.dreamshop.repository.OrderRepository;
import com.example.dreamshop.repository.ProductRepository;

import com.example.dreamshop.response.ApiResponse;
import com.example.dreamshop.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private  final ProductRepository productRepository;
    private final CartService cartService;
    private  final ModelMapper modelMapper;

    @Override
    public Order placeOrder(Long userId)
    {
        Cart cart = cartService.getCartByUserId(userId);

        Order order = creteOrder(cart);
        List<OrderItem> orderItemsList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemsList));
        order.setTotalAmount(calculateTotalAmount(orderItemsList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());

        return savedOrder;
    }

    private Order creteOrder(Cart cart) {
        Order order = new Order();

        order.setUser(cart.getUser());
        String status = String.valueOf(OrderStatus.PENDING);
        order.setOrderStatus(OrderStatus.valueOf(status));
        order.setOrderDate(LocalDate.now());
        return order;

    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getItems()
                .stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();
                    product.setInventory(product.getInventory() - cartItem.getQuantity());
                    productRepository.save(product);
                    return new OrderItem(
                            order,
                            product,
                            cartItem.getQuantity(),
                            cartItem.getUnitPrice());
                }).toList();
    }


    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemsList) {
        return orderItemsList
                .stream()
                .map(item -> item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }
    @Override
    public List<OrderDto> getUserOrder(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return  orders.stream().map(this :: convertToDto).toList();

    }

    private OrderDto convertToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }
}
