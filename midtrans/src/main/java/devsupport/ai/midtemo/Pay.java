package devsupport.ai.midtemo;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shardullavekar on 22/09/17.
 */

public class Pay {
    Activity activity;

    String amount, client_key, merchant_url;

    ApplicationInfo app;

    Bundle bundle;

    MidtransListener listener;

    TransactionFinishedCallback callback;

    JSONObject user;


    public Pay(Activity activity, String amount, JSONObject user, final MidtransListener listener) {
        this.activity = activity;

        this.user = user;

        this.listener = listener;

        this.amount = amount;

        app = null;

        try {
            app = activity.getPackageManager()
                    .getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        bundle = app.metaData;

    }

    public void init() {

        client_key = bundle.getString(Config.CLIENT_KEY);

        merchant_url = bundle.getString(Config.BASE_URL);

        initTxncallback();

        SdkUIFlowBuilder.init(activity.getApplicationContext(), client_key, merchant_url, callback)
                .enableLog(true) // enable sdk log
                .setColorTheme(new CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // will replace theme on snap theme on MAP
                .useBuiltInTokenStorage(true)  // set to false if you want to your own token storage (just for two click)
                .buildSDK();

        TransactionRequest transactionRequest = new TransactionRequest(System.currentTimeMillis() + "", Double.valueOf(amount));

        transactionRequest.setCustomerDetails(initCustomerDetails());

        MidtransSDK.getInstance().setTransactionRequest(transactionRequest);

        MidtransSDK.getInstance().startPaymentUiFlow(activity);
    }

    private void initTxncallback() {
        callback = new TransactionFinishedCallback() {
            @Override
            public void onTransactionFinished(TransactionResult transactionResult) {
                if (TextUtils.equals(transactionResult.getStatus(), Config.SUCCESS)) {
                    listener.onSuccess(transactionResult.getResponse().getOrderId());
                }
                else {
                    listener.onFailure(Integer.valueOf(transactionResult.getResponse().getStatusCode()), transactionResult.getResponse().getStatusMessage());
                }
            }
        };
    }

    private CustomerDetails initCustomerDetails() {

        CustomerDetails mCustomerDetails = new CustomerDetails();

        try {
            mCustomerDetails.setPhone(user.getString("phone"));
            mCustomerDetails.setFirstName(user.getString("name"));
            mCustomerDetails.setEmail(user.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mCustomerDetails;
    }
}
