package com.jimmy.learning

import java.util.UUID

import org.scalatest.{FlatSpec, Matchers}
import io.circe._
import io.circe.syntax._
import io.circe.parser._

class CustomerEncodingSpec extends FlatSpec with Matchers {

  import CustomerEncoding._

  val dataInJsonFormat = """
                           | {
                           |	"orderId" : 2342423,
                           |	"customerInfo" : {
                           |		"accountId" : "239c9d68-e690-11e9-81b4-2a2ae2dbcce4",
                           |		"customerName" : "test-customer",
                           |		"age" : 20
                           |	},
                           |	"itemOrders": [
                           |		{
                           |			"quantity" : 3,
                           |			"item" : {
                           |				"itemName" : "carrot",
                           |				"weight" : 15
                           |			}
                           |		},
                           |		{
                           |			"quantity" : 7,
                           |			"item" : {
                           |				"itemName" : "tomato",
                           |				"weight" : 12
                           |			}
                           |		},
                           |		{
                           |			"quantity" : 1,
                           |			"item" : {
                           |				"itemName" : "onion",
                           |				"weight" : 20
                           |			}
                           |		}
                           |	]
                           |
                           |}
                           |""".stripMargin

  val dataInCaseClass = PurchaseOrder(
    2342423,
    Customer(
      accountId = UUID.fromString("239c9d68-e690-11e9-81b4-2a2ae2dbcce4"),
      name = "test-customer",
      age = 20),
    Seq(
      ItemOrder(3, Item("carrot", 15)),
      ItemOrder(7, Item("tomato", 12)),
      ItemOrder(1, Item("onion", 20))
    )
  )

  "CustomerEncoding" should "be able to encode in new format" in {

    parse(dataInCaseClass.asJson.spaces2) shouldBe parse(dataInJsonFormat)
  }

  it should "decode some valid JSON into a PurchaseOrder" in {

    decode[PurchaseOrder](dataInJsonFormat) shouldBe Right(dataInCaseClass)
  }

}
