package devsupport.ai.midtemo;

/**
 * Created by shardullavekar on 22/09/17.
 */

public interface MidtransListener {
    void onSuccess(String response);
    void onFailure(int code, String reason);
}
