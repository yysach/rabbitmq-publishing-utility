package com.example.rabbitmq.publisher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rabbitmq.client.Channel;

public class MQPublisher implements Runnable {
	private static final Logger logger = LogManager.getLogger(MQPublisher.class);

	private final Channel channel;
	private final String exchangeName;
	private final String routingKey;

	public MQPublisher(final Channel channel, final String exchangeName, final String routingKey) {
		this.channel = channel;
		this.exchangeName = exchangeName;
		this.routingKey = routingKey;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < 100; i++) {
				String message = "Helloworld message - " + i;
				channel.basicPublish(exchangeName, routingKey, null, message.getBytes("UTF-8"));
				logger.info(" [x] Sent '" + message + "'");
				sleepForNextPublish();
			}
		} catch (Exception ex) {
			logger.error("Got an exception while publishing message ", ex);
		}
	}

	private void sleepForNextPublish() {
		try {
			Thread.sleep(2 * 1000L);
		} catch (InterruptedException e) {
			logger.error("Got interrupted exception", e);
		}
	}

}
