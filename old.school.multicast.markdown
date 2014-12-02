





        Internet Addresses
        •
        java.net.
                InetAddress
        class
        •
        You get an address by using static methods:
        ad =
                InetAddress
                        .
                                getByName
                                        (hostname);
        myAddress
                =
                InetAddress
                        .
                                getLocalHost
                                        ();
        4
        Netprog 2002 TCP/IP
        Netprog 2002 TCP/IP
        Printing Internet Addresses
        Printing Internet Addresses
        •
        You get information from an
                InetAddress
        by using methods:
        ad.
                getHostName
                        ();
        ad.
                getHostAddress
                        ();
        •
        Example.
        5
        Netprog 2002 TCP/IP
        Netprog 2002 TCP/IP
        UDP Sockets Programming
        UDP Sockets Programming
        •
        Sending/Receiving data.
        •
        java.net.
                DatagramPacket
        class
        •
        Creating UDP sockets.
        •
        Client
        •
        Server
        •
        java.net.
                DatagramSocket
        class
        6
        Netprog 2002 TCP/IP
        Netprog 2002 TCP/IP
        Creating a UDP packet
        Creating a UDP packet
// to receive data from a remote machine
        DatagramPacket
                packet =
                new
                        DatagramPacket
                        (new byte[256], 256);
// to send data to a remote machine
        DatagramPacket
                packet =
                new
                        DatagramPacket
                        ( new byte[128], 128,
                                address, port );
        7
        Netprog 2002 TCP/IP
        Netprog 2002 TCP/IP
        Creating UDP sockets
        Creating UDP sockets
        •
        A UDP socket can be used both for
        reading and writing packets.
        •
        Write operations are asynchronous;
        however, read operations are blocking.
        •
        Since there is no guaranteed delivery, a
        single
                -
                threaded application could stall.
        8
        Netprog 2002 TCP/IP
        Netprog 2002 TCP/IP
        Creating UDP Sockets
        Creating UDP Sockets
// A client
                datagram
        socket:
        DatagramSocket clientSocket
                =
                new
                        DatagramSocket
                        ();
// A server
        datagram
        socket:
        DatagramSocket serverSocket
                =
                new
                        DatagramSocket
                        (port);
        9
        Netprog 2002 TCP/IP
        Netprog 2002 TCP/IP
        Listening for UDP Packets
        Listening for UDP Packets
// create
        datagram
                packet
        . . .
// create
        datagram
        server socket
        . . .
        boolean
                finished = false;
        while ( ! finished ) {
            serverSocket
                    .receive (packet);
// process the packet
        }
        serverSocket
                .close();
        10
        Netprog 2002 TCP/IP
        Netprog 2002 TCP/IP
        Processing UDP Packets
        Processing UDP Packets
                ByteArrayInputStream
        bin =
                new
                        ByteArrayInputStream
                        (
                                packet.
                                        getData
                                                () );
        DataInputStream
                din =
                new
                        DataInputStream
                        (bin);
// read the contents of the packet
        11
        Netprog 2002 TCP/IP
        Netprog 2002 TCP/IP
        Sending UDP Packets
        Sending UDP Packets
// create
                datagram
        packet
                . . .
// create
        datagram
        client socket
        . . .
        boolean
                finished = false;
        while ( ! finished ) {
// write data to packet buffer
            clientSocket
                    .send (packet);
// see if there is more to send
        }
        12
        Netprog 2002 TCP/IP
        Netprog 2002 TCP/IP
        Sending UDP packets
        Sending UDP packets
        •
        When you receive a packet, the
        ip
                and
        port number of the sender are set in the
                DatagramPacket
        .
        •
        You can use the same packet to reply,
        by overwriting the data, using the
        method:
        •
        packet.
                setData
                        (
                                newbuffer
                        );
