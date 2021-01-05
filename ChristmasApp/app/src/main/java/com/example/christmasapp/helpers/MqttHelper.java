package com.example.christmasapp.helpers;

import android.os.Bundle;
import android.util.Log;

import com.example.christmasapp.ChristmasActivity;
import com.example.christmasapp.NotificationManager;
import com.example.christmasapp.data.model.NotificationDTO;
import com.example.christmasapp.utils.Constants;
import com.example.christmasapp.utils.Utils;
import com.example.christmasapp.tasks.SaveNotificationTask;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.Serializable;

public class MqttHelper implements Serializable {

    private static MqttHelper mqttHelper = null;

    private final String serverUri = "tcp://broker.mqttdashboard.com:1883";
    private final String clientId = "ChristmasAppUC";
    private final NotificationManager notificationManager;
    private final ChristmasActivity christmasActivity;
    public MqttAndroidClient mqttAndroidClient;

    public static MqttHelper getInstance(ChristmasActivity christmasActivity) {
        if (mqttHelper == null)
        {
            mqttHelper = new MqttHelper(christmasActivity);
        }
        return mqttHelper;
    }

    private MqttHelper(ChristmasActivity christmasActivity) {
        this.christmasActivity = christmasActivity;
        this.notificationManager = christmasActivity;

        mqttAndroidClient = new MqttAndroidClient(christmasActivity.getBaseContext(), serverUri, clientId);

        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.CONNECTION_STATUS_KEY, false);
                notificationManager.notifyConnection(bundle);
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) {
                    NotificationDTO notificationDTO = Utils.deserializeNotification(new String(mqttMessage.getPayload()));
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.OPERATION_KEY, Constants.CREATE_NOTIFICATION_MODE);
                    bundle.putSerializable(Constants.NOTIFICATION_KEY, notificationDTO);
                    bundle.putSerializable(Constants.ACTIVITY_KEY, christmasActivity);
                    new SaveNotificationTask(bundle).execute();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        connect();
    }

    private void connect() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);

        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constants.CONNECTION_STATUS_KEY, true);
                    notificationManager.notifyConnection(bundle);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constants.CONNECTION_STATUS_KEY, false);
                    notificationManager.notifyConnection(bundle);
                }
            });
        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    public void subscribeToTopic(String subscriptionTopic) {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w("Mqtt", "Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Subscribed fail!");
                }
            });

        } catch (MqttException ex) {
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    public void unSubscribeToTopic(String subscriptionTopic) {
        try {
            mqttAndroidClient.unsubscribe(subscriptionTopic);
        } catch (MqttException ex) {
            System.err.println("Exception whilst unsubscribe");
            ex.printStackTrace();
        }
    }

//    public void publishToTopic(String subscriptionTopic, byte[] message) {
//        try {
//            IMqttDeliveryToken publish = mqttAndroidClient.publish(subscriptionTopic, message, 0, true);
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }
}
