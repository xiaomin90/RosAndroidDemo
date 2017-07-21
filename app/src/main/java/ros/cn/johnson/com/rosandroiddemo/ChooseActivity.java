package ros.cn.johnson.com.rosandroiddemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import ros.cn.johnson.com.rosandroiddemo.camera.CameraMainActivity;
import ros.cn.johnson.com.rosandroiddemo.cameratransport.CameraTransportMainActivity;
import ros.cn.johnson.com.rosandroiddemo.pubsub.PubSubMainActivity;

/**
 * Created by Administrator on 2017/7/21.
 */

public class ChooseActivity extends Activity {


    private Button btnPubSub;
    private Button btnCamera;
    private Button btnCameraTransport;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);

        btnPubSub = (Button) this.findViewById(R.id.pubsub);
        btnCamera = (Button) this.findViewById(R.id.camera);
        btnCameraTransport = (Button) this.findViewById(R.id.cameratransport);
        this.context = this;
        btnCameraTransport.setOnClickListener(clickListener);
        btnCamera.setOnClickListener(clickListener);
        btnPubSub.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.pubsub:
                    intent = new Intent(context, PubSubMainActivity.class);
                    break;
                case R.id.camera:
                    intent = new Intent(context, CameraMainActivity.class);
                    break;
                case R.id.cameratransport:
                    intent = new Intent(context, CameraTransportMainActivity.class);
                    break;
                default:
                    break;
            }
            if(intent != null)
                context.startActivity(intent);
        }
    };

}
