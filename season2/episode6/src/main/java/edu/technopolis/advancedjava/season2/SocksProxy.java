package edu.technopolis.advancedjava.season2;

import java.nio.channels.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class SocksProxy {

    static ArrayList <SocksClient> clients = new ArrayList<SocksClient>();

    public SocksClient addClient(SocketChannel s) {
        SocksClient cl;
        try {
            cl = new SocksClient(s);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        clients.add(cl);
        return cl;
    }

    public SocksProxy() throws IOException {
        ServerSocketChannel socks = ServerSocketChannel.open();
        socks.socket().bind(new InetSocketAddress(8008));
        socks.configureBlocking(false);
        Selector select = Selector.open();
        socks.register(select, SelectionKey.OP_ACCEPT);

        int lastClients = clients.size();
        while(true) {
            select.select(1000);

            Set keys = select.selectedKeys();
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey k = (SelectionKey) iterator.next();

                if (!k.isValid())
                    continue;

                // new connection?
                if (k.isAcceptable() && k.channel() == socks) {
                    // server socket
                    SocketChannel csock = socks.accept();
                    if (csock == null)
                        continue;
                    addClient(csock);
                    csock.register(select, SelectionKey.OP_READ);
                } else if (k.isReadable()) {
                    for (int i = 0; i < clients.size(); i++) {
                        SocksClient cl = clients.get(i);
                        try {
                            if (k.channel() == cl.client)
                                cl.newClientData(select, k);
                            else if (k.channel() == cl.remote) {
                                cl.newRemoteData(select, k);
                            }
                        } catch (IOException e) {
                            cl.client.close();
                            if (cl.remote != null)
                                cl.remote.close();
                            k.cancel();
                            clients.remove(cl);
                        }

                    }
                }
            }

            for (int i = 0; i < clients.size(); i++) {
                SocksClient cl = clients.get(i);
                if((System.currentTimeMillis() - cl.lastData) > 30000L) {
                    cl.client.close();
                    if(cl.remote != null)
                        cl.remote.close();
                    clients.remove(cl);
                }
            }
            if(clients.size() != lastClients) {
                System.out.println(clients.size());
                lastClients = clients.size();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new SocksProxy();
    }
}