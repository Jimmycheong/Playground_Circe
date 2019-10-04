package com.jimmy

import java.util.UUID

package object learning {
  case class Customer(accountId: UUID, name: String, age: Int)

  case class Item(name: String, weight: Int)

  case class ItemOrder(quantity: Int, item: Item)

  case class PurchaseOrder(orderId: Int, customer: Customer, itemOrders: Seq[ItemOrder])
  // Entity to encode/decode

}
