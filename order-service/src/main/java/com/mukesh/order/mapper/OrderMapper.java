package com.mukesh.order.mapper;

import com.mukesh.order.dto.CreateOrderRequest;
import com.mukesh.order.dto.OrderItemRequest;
import com.mukesh.order.dto.OrderItemResponse;
import com.mukesh.order.dto.OrderResponse;
import com.mukesh.order.entity.OrderEntity;
import com.mukesh.order.entity.OrderItemEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    /*
     * ===========================
     * Request DTO -> Entity
     * ===========================
     */

    OrderEntity toOrderEntity(CreateOrderRequest request);

    OrderItemEntity toOrderItemEntity(OrderItemRequest request);

    /*
     * ===========================
     * Entity -> Response DTO
     * ===========================
     */

    OrderResponse toResponse(OrderEntity entity);

    OrderItemResponse toResponse(OrderItemEntity entity);

    /*
     * ===========================
     * Build Aggregate
     * ===========================
     */

    default OrderEntity buildOrder(CreateOrderRequest request) {

        OrderEntity order = toOrderEntity(request);

        if (request.items() != null) {

            request.items()
                    .stream()
                    .map(this::toOrderItemEntity)
                    .forEach(order::addItem);

        }

        return order;
    }

    /*
     * ===========================
     * Maintain Bi-directional Relation
     * ===========================
     */

    @AfterMapping
    default void linkOrderItems(
            @MappingTarget OrderEntity order
    ) {

        if (order.getItems() == null) {
            return;
        }

        order.getItems()
                .forEach(item -> item.setOrder(order));
    }

}