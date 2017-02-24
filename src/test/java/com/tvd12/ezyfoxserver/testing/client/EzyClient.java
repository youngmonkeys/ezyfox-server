/**
 * 
 */
package com.tvd12.ezyfoxserver.testing.client;

import java.net.InetSocketAddress;

import com.tvd12.ezyfoxserver.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.codec.MsgPackCodecCreator;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AllArgsConstructor;

/**
 * @author tavandung12
 *
 */
@AllArgsConstructor
public class EzyClient {

    private final String host;
    private final int port;
    
    public static void main(String[] args)
            throws Exception {
            if (args.length != 2) {
                System.err.println("Usage: " + EzyClient.class.getSimpleName() +
                    " <host> <port>"
                );
                return;
            }

            final String host = args[0];
            final int port = Integer.parseInt(args[1]);
            new EzyClient(host, port).start();
        }
    
    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializerImpl());
            System.out.println("client connecting...");
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        }
        finally {
            group.shutdownGracefully().sync();
        }
    }
    
    private static final EzyCodecCreator CODEC_CREATOR = new MsgPackCodecCreator();
    
    public final class ChannelInitializerImpl extends ChannelInitializer<Channel> {
		@Override
		protected void initChannel(Channel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast(CODEC_CREATOR.newEncoder());
			pipeline.addLast(new EzyClientHandler());
			pipeline.addLast(CODEC_CREATOR.newDecoder());
		}
	}
    
}
