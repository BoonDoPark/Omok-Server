package com.example.omok.socket;


import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Component
public class AsyncSocketServer {
    private List<SocketChannel> socketList = new ArrayList<>();

    public void asyncServerSocket() {
        List<SocketChannel> socketList = new ArrayList<>();

        try (ServerSocketChannel server = ServerSocketChannel.open()) {
            server.bind(new InetSocketAddress(8081));
            Selector selector = Selector.open();

            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while(true) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keys = selectionKeys.iterator();

                while(keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (key.isAcceptable()) {
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                        socketList.add(client);
                    }
                    if (key.isReadable()) {
                        SocketChannel sender = (SocketChannel) key.channel();
                        if (sender.read(buffer) == -1) {
                            sender.close();
                            socketList.remove(sender);
                            System.out.println("클라이언트 연결 종료");
                            continue;
                        }

                        buffer.flip();
                        if (buffer.remaining() >= 4) { // 예: int(4바이트)를 기대할 경우
                            int roomId = buffer.get() & 0xFF;
                            int x = buffer.get() & 0xFF;
                            int y = buffer.get() & 0xFF;

                            int value = (roomId << 24) | (x << 16) | (y << 8);
                        }

                        for(SocketChannel socket : socketList) {
                            if(socket != sender) {
                                socket.write(buffer);
                            }
                        }
                        buffer.clear();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
