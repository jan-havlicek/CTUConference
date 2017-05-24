/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.websocket.endpoint;

import org.kurento.client.KurentoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Nick Nemame
 */
@Configuration
public class KurentoConfig {

    //public static final String DEFAULT_KMS_WS_URI = "ws://192.168.56.101:8888/kurento";
    public static final String DEFAULT_KMS_WS_URI = "ws://147.32.80.232:8888/kurento";

    @Bean
    public KurentoClient kurentoClient() {
        return KurentoClient.create(System.getProperty("kms.ws.uri", DEFAULT_KMS_WS_URI));
    }

}
