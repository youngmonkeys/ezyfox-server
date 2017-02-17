/**
 * 
 */
package com.tvd12.ezyfoxserver.temp;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AllArgsConstructor;

/**
 * @author tavandung12
 *
 */
@AllArgsConstructor
public class EchoServer {

	private int port;

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
			return;
		}
		int port = Integer.parseInt(args[0]);
		new EchoServer(port).start();
	}

	public void start() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap()
					.group(group)
					.channel(NioServerSocketChannel.class)
					.localAddress(new InetSocketAddress(port))
					.childHandler(new ChannelInitializerImpl());

			ChannelFuture f = b.bind().sync();
			System.out.println(getClass().getName() + " " + " started and listening for connections on "
					+ f.channel().localAddress());
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}

	public final class ChannelInitializerImpl extends ChannelInitializer<Channel> {
		@Override
		protected void initChannel(Channel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast(new EchoServerHandler());
		}
	}
}
