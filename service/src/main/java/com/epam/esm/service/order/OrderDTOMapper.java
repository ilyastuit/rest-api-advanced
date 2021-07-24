package com.epam.esm.service.order;

import com.epam.esm.entity.order.Order;
import com.epam.esm.entity.order.OrderDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDTOMapper {

    OrderDTO orderToOrderDTO(Order order);

    List<OrderDTO> orderListToOrderDTOList(List<Order> orderList);

}
