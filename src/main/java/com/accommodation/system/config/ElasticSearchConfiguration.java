package com.accommodation.system.config;

import com.accommodation.system.define.Constant;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;

@Configuration
@Slf4j
public class ElasticSearchConfiguration {
    @Bean
    public Client client() {
        try {
            String host = Constant.ELASTIC_HOST;
            Settings settings = Settings.builder().put("cluster.name", "elasticsearch")
                    .put("client.transport.ping_timeout", "5s")
                    .put("transport.connect_timeout", "5s")
                    .put("transport.tcp.connect_timeout", "5s")
                    .build();
            TransportClient client = new PreBuiltTransportClient(settings);
            client.addTransportAddress(new TransportAddress(InetAddress.getByName(host), 9300));
            return client;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
