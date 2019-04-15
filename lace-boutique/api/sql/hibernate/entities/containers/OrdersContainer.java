package api.sql.hibernate.entities.containers;

import java.util.List;

import api.sql.hibernate.entities.ItemSpec;
import api.sql.hibernate.entities.Order;
import api.sql.hibernate.entities.OrderItem;

public class OrdersContainer {
	
	private Order order;
	
	private List<OrderItem> orderItems;
	
	public OrdersContainer(Order order, List<OrderItem> orderItems) {
		this.order = order;
		this.orderItems = orderItems;
	}

	public Order getOrder()
	{
		return order;
	}

	public void setOrder(Order order)
	{
		this.order = order;
	}

	public List<OrderItem> getOrderItems()
	{
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems)
	{
		this.orderItems = orderItems;
	}

	@Override
	public String toString()
	{
		return "OrdersContainer [order=" + order + ", orderItems=" + orderItems + "]";
	}
	
}
