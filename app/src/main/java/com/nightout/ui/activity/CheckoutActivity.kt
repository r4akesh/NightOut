package com.nightout.ui.activity

import android.os.Bundle
import com.nightout.R
import com.nightout.base.BaseActivity
import com.nightout.model.PlaceOrderResponse
import com.nightout.utils.AppConstant
import com.nightout.utils.MyApp
import com.nightout.utils.Utills
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult

lateinit var paymentSheet: PaymentSheet
lateinit var customerConfig: PaymentSheet.CustomerConfiguration
lateinit var paymentIntentClientSecret: String

class CheckoutActivity:BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentSheet = PaymentSheet(THIS!!, ::onPaymentSheetResult)

        var fetchData = intent.getSerializableExtra(AppConstant.INTENT_EXTRAS.PLACEORDER_RES) as PlaceOrderResponse.Data
        PaymentConfiguration.init(this, resources.getString(R.string.stripe_publshKey))
        paymentIntentClientSecret= fetchData.payment_intent_key
        customerConfig = PaymentSheet.CustomerConfiguration(fetchData.customer_id,fetchData.ephemeralKey)
        presentPaymentSheet()
    }


    fun presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret,
            PaymentSheet.Configuration(
                merchantDisplayName = "My merchant name",
                customer = customerConfig,
                // Set `allowsDelayedPaymentMethods` to true if your business
                // can handle payment methods that complete payment after a delay, like SEPA Debit and Sofort.
                allowsDelayedPaymentMethods = false//show hide other card option
            )
        )
    }
    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when(paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
           //  Utills.showDefaultToast(THIS!!,)
                Utills.showDefaultToast(THIS!!,"Payment Canceled")
                finish()
            }
            is PaymentSheetResult.Failed -> {
                Utills.showDefaultToast(THIS!!,"${paymentSheetResult.error}")
                print("Error: ${paymentSheetResult.error}")
                finish()
            }
            is PaymentSheetResult.Completed -> {
                // Display for example, an order confirmation screen
                Utills.showDefaultToast(THIS!!,"Payment Successfully Done")
                finish()
            }
        }
    }
}