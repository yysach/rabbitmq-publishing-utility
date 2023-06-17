package com.example.rabbitmq;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.rabbitmq.publisher.MQPublisher;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class MQWriteChannel {
	private static final Logger logger = LogManager.getLogger(MQWriteChannel.class);

	private static final int PREFETCH_COUNT = 1;
	private static final boolean DURABLE = true;
	private static final boolean EXCLUSIVE = false;
	private static final boolean AUTO_DELETE = false;
	private static final Map<String, Object> ARGS = Collections.singletonMap("x-message-ttl",
			(Object) Integer.valueOf(900000));

	private final String queueName;
	private final String routingKey;
	private final boolean createQueue;

	public MQWriteChannel(String messageQueue, boolean createQueue) {
		// TODO: providing same routing key as queueName
		this.queueName = messageQueue;
		this.routingKey = messageQueue;
		this.createQueue = createQueue;
	}

	private void bindQueue(Channel channel, String exchangeName) throws IOException {
		if (this.createQueue) {
			channel.queueDeclare(this.queueName, DURABLE, EXCLUSIVE, AUTO_DELETE, ARGS);
		}
		channel.queueBind(this.queueName, exchangeName, this.routingKey);
		channel.basicQos(PREFETCH_COUNT);
	}

	public void openWriteChannel(Connection connection, String exchangeName) throws IOException, TimeoutException {
		final Channel channel = connection.createChannel();
		// declare fanout exchange
		channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);
		bindQueue(channel, exchangeName);

		// thread to publish series of messages.
		Runnable runnable = new MQPublisher(channel, exchangeName, this.routingKey);
		Thread thread = new Thread(runnable);
		thread.start();

		// Main thread has to wait till publishing thread done with pushing messages.
		waitForPublishingTheadToComplete(thread);
		channel.close();
	}

	private void waitForPublishingTheadToComplete(Thread thread) {
		try {
			thread.join();
		} catch (InterruptedException ex) {
			logger.error("Got interruptedException ", ex);
		}

	}
}
