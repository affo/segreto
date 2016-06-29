package it.polimi.nwlus.segreto.kafka.streams;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

/**
 * Created by affo on 28/06/16.
 */
public class MyExtractor implements TimestampExtractor {
    @Override
    public long extract(ConsumerRecord<Object, Object> record) {
        String message = (String) record.value();
        String[] values = message.trim().replace(")", "").replace("(", "").split(",");
        Long timestamp = Long.parseLong(values[0]) * 1000L;
        return timestamp;
    }
}
