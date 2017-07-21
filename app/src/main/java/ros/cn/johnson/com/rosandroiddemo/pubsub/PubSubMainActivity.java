package ros.cn.johnson.com.rosandroiddemo.pubsub;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import org.ros.android.MessageCallable;
import org.ros.android.RosActivity;
import org.ros.android.view.RosTextView;
import org.ros.internal.node.topic.SubscriberIdentifier;
import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.PublisherListener;
import org.ros.node.topic.Subscriber;
import org.ros.rosjava_tutorial_pubsub.Talker;

import ros.cn.johnson.com.rosandroiddemo.R;
import ros.cn.johnson.com.rosandroiddemo.RosDebugLog;
import std_msgs.String;

public class PubSubMainActivity extends RosActivity {

    private RosTextView<String> rosTextView;
    private Talker talker;

    private Button btnpub;
    private TextView textsub;

    private Publisher<std_msgs.String> publisher;
    private Subscriber<std_msgs.String> subscriber;
    private ConnectedNode node;

    public PubSubMainActivity() {
        // The RosActivity constructor configures the notification title and ticker
        // messages.
        super("Pubsub Tutorial", "Pubsub Tutorial");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pub_sub);
        rosTextView = (RosTextView<std_msgs.String>) findViewById(R.id.text);
        rosTextView.setTopicName("chatter");
        rosTextView.setMessageType(std_msgs.String._TYPE);
        rosTextView.setMessageToStringCallable(new MessageCallable<java.lang.String,std_msgs.String>() {
            @Override
            public java.lang.String call(std_msgs.String message) {
                return message.getData();
            }
        });

        btnpub = (Button) this.findViewById(R.id.btnpub);
        textsub = (TextView) this.findViewById(R.id.textsub);

        btnpub.setOnClickListener(new View.OnClickListener() {
            private int i = 0;
            @Override
            public void onClick(View v) {
                if(node == null)
                    node = rosTextView.getNode();
                if(node == null) {
                    RosDebugLog.d(this.getClass().toString()," node is null");
                    return;
                }
                if(subscriber == null) {
                    subscriber = node.newSubscriber("chatter2",String._TYPE);
                    subscriber.addMessageListener(new MessageListener<String>() {
                        @Override
                        public void onNewMessage(final String msg) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textsub.setText(msg.getData());
                                }
                            });
                        }
                    });
                }

                if(publisher == null) {
                    publisher = node.newPublisher("chatter2", String._TYPE);
                    publisher.addListener(new PublisherListener<String>() {
                        @Override
                        public void onNewSubscriber(Publisher<String> publisher, SubscriberIdentifier subscriberIdentifier) {
                            RosDebugLog.d(this.getClass().toString()," onNewSubscriber");
                        }

                        @Override
                        public void onShutdown(Publisher<String> publisher) {
                            RosDebugLog.d(this.getClass().toString()," onShutdown");
                        }

                        @Override
                        public void onMasterRegistrationSuccess(Publisher<String> stringPublisher) {
                            RosDebugLog.d(this.getClass().toString()," onMasterRegistrationSuccess");
                        }

                        @Override
                        public void onMasterRegistrationFailure(Publisher<String> stringPublisher) {
                            RosDebugLog.d(this.getClass().toString()," onMasterRegistrationFailure");
                        }

                        @Override
                        public void onMasterUnregistrationSuccess(Publisher<String> stringPublisher) {
                            RosDebugLog.d(this.getClass().toString()," onMasterUnregistrationSuccess");
                        }

                        @Override
                        public void onMasterUnregistrationFailure(Publisher<String> stringPublisher) {
                            RosDebugLog.d(this.getClass().toString()," onMasterUnregistrationFailure");
                        }
                    });
                }
                if(publisher == null ) {
                    RosDebugLog.d(this.getClass().toString()," publishber is null");
                    return;
                }
                java.lang.String content = java.lang.String.format("context pub %d",i++);
                std_msgs.String msg =publisher.newMessage();
                msg.setData(content);
                RosDebugLog.d(this.getClass().toString(),"pub msg : " + content);
                publisher.publish(msg);
            }
        });


    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        talker = new Talker();

        // At this point, the user has already been prompted to either enter the URI
        // of a master to use or to start a master locally.

        // The user can easily use the selected ROS Hostname in the master chooser
        // activity.
        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
        nodeConfiguration.setMasterUri(getMasterUri());
        nodeMainExecutor.execute(talker, nodeConfiguration);
        // The RosTextView is also a NodeMain that must be executed in order to
        // start displaying incoming messages.
        nodeMainExecutor.execute(rosTextView, nodeConfiguration);
    }
}