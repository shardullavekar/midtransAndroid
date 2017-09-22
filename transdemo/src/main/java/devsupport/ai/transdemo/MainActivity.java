package devsupport.ai.transdemo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import devsupport.ai.midtemo.MidtransListener;
import devsupport.ai.midtemo.Pay;

public class MainActivity extends AppCompatActivity {

    MidtransListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        midpay("tester@gmail.com", "+919591953812", "shardul lavekar", "900");
    }

    private void midpay(String email, String phone, String name, String amount) {
        Activity activity = this;

        initListener();

        JSONObject user = new JSONObject();

        try {
            user.put("email", email);

            user.put("name", name);

            user.put("phone", phone);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Pay pay = new Pay(activity, amount, user, listener);

        pay.init();
    }

    private void initListener() {
        listener = new MidtransListener() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onFailure(int code, String reason) {
                Toast.makeText(getApplicationContext(), reason, Toast.LENGTH_LONG)
                        .show();
            }
        };
    }
}
