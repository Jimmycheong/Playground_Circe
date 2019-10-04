package com.jimmy.learning
import java.util.UUID

import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, Json}

object CustomerEncoding {

  implicit val purchaseOrderEncoder: Encoder[PurchaseOrder] = new Encoder[PurchaseOrder] {
    override def apply(po: PurchaseOrder): Json = {

      Json.obj(
        ("orderId", Json.fromInt(po.orderId)),
        ("customerInfo", Json.obj(
          ("accountId", Json.fromString(po.customer.accountId.toString)),
          ("customerName", Json.fromString(po.customer.name)),
          ("age", Json.fromInt(po.customer.age))
        )),
        ("itemOrders", Json.fromValues(
          po.itemOrders.map((itemOrder: ItemOrder) => Json.obj(
            ("quantity", Json.fromInt(itemOrder.quantity)),
            ("item", Json.obj(
              ("itemName", Json.fromString(itemOrder.item.name)),
              ("weight", Json.fromInt(itemOrder.item.weight))
            ))
          ))
        ))
      )
    }
  }

  implicit val purchaseOrderDecoder: Decoder[PurchaseOrder] = new Decoder[PurchaseOrder] {
    override def apply(c: HCursor): Result[PurchaseOrder] = {
      for {
        orderId             <- c.get[Int]("orderId")
        customerAccountId   <- c.downField("customerInfo").get[UUID]("accountId")
        customerAccountName <- c.downField("customerInfo").get[String]("customerName")
        customerAge         <- c.downField("customerInfo").get[Int]("age")
        itemOrders <- c.downField("itemOrders").as[Seq[ItemOrder]]
      } yield {
        PurchaseOrder(
          orderId = orderId,
          customer = Customer(
            customerAccountId,
            customerAccountName,
            customerAge
          ),
          itemOrders = itemOrders
        )
      }
    }
  }

  private implicit val itemOrderDecoder: Decoder[ItemOrder] = new Decoder[ItemOrder] {
    override def apply(c: HCursor): Result[ItemOrder] = {
      for {
        quantity <- c.downField("quantity").as[Int]
        name <- c.downField("item").downField("itemName").as[String]
        weight <- c.downField("item").downField("weight").as[Int]
      } yield ItemOrder(quantity, Item(name, weight))
    }
  }
}
