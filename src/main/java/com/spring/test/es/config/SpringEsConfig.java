package com.spring.test.es.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;

/**
 * Created by sunboyu on 2018/2/7.
 */
@Configuration
public class SpringEsConfig {

    @Bean
    public TransportClient client() throws Exception {
        Settings settings = Settings.builder()
                //设置ES实例的名称
                .put("cluster.name", "elasticsearch")
                //自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
                .put("client.transport.sniff", true).build();

        //初始化client较老版本发生了变化，此方法有几个重载方法，初始化插件等。
        TransportClient client = new PreBuiltTransportClient(settings);
        //此步骤添加IP，至少一个，其实一个就够了，因为添加了自动嗅探配置
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.124.128"), 9300));
        return client;
    }
}
