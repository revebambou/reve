package cn.reve.rabbit;

import com.alibaba.fastjson.JSON;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class MessageConsumer implements MessageListener {
    @Override
    public void onMessage(Message message) {
        String str = new String(message.getBody());
        Map map = JSON.parseObject(str, Map.class);
        System.out.println(map);
    }
}
