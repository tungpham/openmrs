/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.api.handler;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Order;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseContextSensitiveTest;
import org.openmrs.test.Verifies;

/**
 * Contains the methods for testing the {@link OrderSaveHandler}
 */
public class OrderSaveHandlerTest extends BaseContextSensitiveTest {
	
	/**
	 * @see {@link OrderSaveHandler#handle(Order,User,Date,String)}
	 */
	@Test
	@Verifies(value = "should set the order number", method = "handle(Order,User,Date,String)")
	public void handle_shouldSetTheOrderNumber() throws Exception {
		Order order = new Order();
		new OrderSaveHandler().handle(order, null, null, null);
		Assert.assertNotNull(order.getOrderNumber());
	}
	
	/**
	 * @see {@link OrderSaveHandler#handle(Order,User,Date,String)}
	 */
	@Test
	@Verifies(value = "should not assign an new order number to an existing order", method = "handle(Order,User,Date,String)")
	public void handle_shouldNotAssignAnNewOrderNumberToAnExistingOrder() throws Exception {
		Order order = Context.getOrderService().getOrder(1);
		Assert.assertNotNull(order.getOrderNumber());
		String oldOrderNumber = order.getOrderNumber();
		
		new OrderSaveHandler().handle(order, null, null, null);
		Assert.assertEquals(oldOrderNumber, order.getOrderNumber());
	}
}
