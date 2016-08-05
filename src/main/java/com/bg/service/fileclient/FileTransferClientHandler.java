package com.bg.service.fileclient;


import com.alibaba.fastjson.JSONObject;
import com.bg.model.fileclient.RequestFile;
import com.bg.model.fileclient.ResponseFile;
import com.bg.model.fileclient.SecureModel;
import com.bg.util.JedisAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileTransferClientHandler extends ChannelInboundHandlerAdapter {
	private int byteRead;
	private volatile long start = 0;
	public RandomAccessFile randomAccessFile;
	private RequestFile request;
	private final int minReadBufferSize = 8192 * 1000;

	JedisAdapter jedisAdapter = new JedisAdapter();

	public FileTransferClientHandler(RequestFile ef) {
		if (ef.getFile().exists()) {
			if (!ef.getFile().isFile()) {
				System.out.println("Not a file :" + ef.getFile());
				return;
			}
		}
		this.request = ef;
	}

	public void channelActive(ChannelHandlerContext ctx) {
		/*try {
			randomAccessFile = new RandomAccessFile(request.getFile(), "r");
			randomAccessFile.seek(request.getStarPos());
			byte[] bytes = new byte[minReadBufferSize];
			if ((byteRead = randomAccessFile.read(bytes)) != -1) {
				request.setEndPos(byteRead);
				request.setBytes(bytes);
				request.setFile_size(randomAccessFile.length());
				ctx.writeAndFlush(request);
			} else {
				System.out.println("文件已经读完");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException i) {
			i.printStackTrace();
		}*/
		SecureModel secure = new SecureModel();
		secure.setToken("2222222222222");
		ctx.writeAndFlush(secure);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			if (msg instanceof com.bg.model.fileclient.SecureModel) {
				try {
					randomAccessFile = new RandomAccessFile(request.getFile(), "r");
					randomAccessFile.seek(request.getStarPos());
					byte[] bytes = new byte[minReadBufferSize];
					if ((byteRead = randomAccessFile.read(bytes)) != -1) {
						request.setEndPos(byteRead);
						request.setBytes(bytes);
						request.setFile_size(randomAccessFile.length());
						ctx.writeAndFlush(request);
					} else {
						System.out.println("over");
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException i) {
					i.printStackTrace();
				}
				return;
			}

			if (msg instanceof ResponseFile) {
				ResponseFile response = (ResponseFile) msg;
				System.out.println(response);
				jedisAdapter.lpush("response", JSONObject.toJSONString(response));


				if (response.isEnd()) {
					randomAccessFile.close();
					ctx.close();
				} else {
					start = response.getStart();
					if (start != -1) {
						randomAccessFile = new RandomAccessFile(request.getFile(), "r");
						randomAccessFile.seek(start);
						int a;
						if((randomAccessFile.length() - start) > Integer.MAX_VALUE){
							a =Integer.MAX_VALUE;
						}
						else a = (int) (randomAccessFile.length() - start);
						int sendLength = minReadBufferSize;
						if (a < minReadBufferSize) {
							sendLength = a;
						}
						byte[] bytes = new byte[sendLength];
						if ((byteRead = randomAccessFile.read(bytes)) != -1 && (randomAccessFile.length() - start) > 0) {
							request.setEndPos(byteRead);
							request.setBytes(bytes);
							try {
								ctx.writeAndFlush(request);
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							randomAccessFile.close();
							ctx.close();
						}
					}
				}
			}
		}



	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

}
