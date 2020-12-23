package com.example.christmasapp.helpers;


import android.os.Bundle;
import android.util.Log;

import com.example.christmasapp.ChristmasActivity;
import com.example.christmasapp.NotificationManager;
import com.example.christmasapp.data.Utils;
import com.example.christmasapp.data.model.Notification;

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

    private final String serverUri = "tcp://broker.mqttdashboard.com:1883";
    private final String clientId = "clientId-PpPdTaXO2b";
    private final DatabaseHelper databaseHelper;
    private final NotificationManager notificationManager;
    public MqttAndroidClient mqttAndroidClient;

    public MqttHelper(DatabaseHelper databaseHelper, ChristmasActivity mainActivity) {
        this.databaseHelper = databaseHelper;
        this.notificationManager = mainActivity;

        mqttAndroidClient = new MqttAndroidClient(mainActivity.getBaseContext(), serverUri, clientId);


        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Utils.CONNECTION_STATUS_KEY, false);
                notificationManager.notifyConnection(bundle);
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {

//                    Notification notification = Utils.deserializeNotification(new String(mqttMessage.getPayload()));
                    Bundle bundle = new Bundle();
//                    bundle.putString(Utils.OPERATION_KEY, Utils.CREATE_NOTIFICATION_MODE);
//                    bundle.putString(Utils.NOTIFICATION_TITLE_KEY, notification.getTitle());
//                    bundle.putString(Utils.NOTIFICATION_DESCRIPTION_KEY, notification.getDescription());
                    notificationManager.newNotification(bundle);

                Log.d("RECEIVED", new String(mqttMessage.getPayload()));
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
                    bundle.putBoolean(Utils.CONNECTION_STATUS_KEY, true);
                    notificationManager.notifyConnection(bundle);
                    Log.d("MQTT", "Connected successfully to: " + serverUri);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Utils.CONNECTION_STATUS_KEY, false);
                    notificationManager.notifyConnection(bundle);
                    Log.d("MQTT", "Failed to connect to: " + serverUri + exception.toString());
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

    public void publishToTopic(String subscriptionTopic, byte[] message) {
        try {
            IMqttDeliveryToken publish = mqttAndroidClient.publish(subscriptionTopic, message, 0, true);
            Log.d("RESULTADO", publish.toString());
            Log.d("PUBLISHED", new String(message));
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
