package com.accommodation.system.config;

import com.accommodation.system.define.Constant;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

@Configuration
@Slf4j
public class ElasticSearchConfiguration {
    private final Environment env;

    @Autowired
    public ElasticSearchConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    public Client client() {
        try {
            String host = "search-accommodation-system-gp25tddwrv3mounemwcxbatyzy.ap-southeast-1.es.amazonaws.com";
            Settings settings = Settings.builder().put("cluster.name", Constant.EsConfiguration.ELASTICSEARCH_CLUSTER_NAME)
                    .put("client.transport.ping_timeout", env.getProperty("client.transport.ping_timeout", "5s"))
                    .put("transport.connect_timeout", env.getProperty("transport.connect_timeout", "5s"))
                    .put("transport.tcp.connect_timeout", env.getProperty("transport.tcp.connect_timeout", "5s"))
                    .build();
            TransportClient client = new PreBuiltTransportClient(settings);
            client.addTransportAddress(new TransportAddress(InetAddress.getByName(host), 9300)); //for version 6.3

            return client;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
