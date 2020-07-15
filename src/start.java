// main function to launch threaded servers and the requesting client process
// takes one command line argument which is the hostfile name
//
// @author: Logan D'Auria
// @email:  lxd1644@rit.edu

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class start {

    // constant across program
    public static final int PORT_NUMBER = 5000;

    // stores ips from hostfile
    private static ArrayList<String> servers;

    public static void main(String[] args) {

        servers = new ArrayList<>();

        // Reads server addresses from server file
        try {
            File serverfile = new File(args[0]);
            BufferedReader fileReader = new BufferedReader(new FileReader(serverfile));
            String s;
            while((s = fileReader.readLine()) != null){
                servers.add(s.trim());
            }
        } catch (Exception e) {
            System.err.println("Error reading server address file");
            System.exit(1);
        }

        // Starts the 4 server threads
        for(int x = 0; x < 4; x++) {
            server s = new server(x, servers.get(x), PORT_NUMBER, servers.get(0));
            Thread serverThread = new Thread(s);
            serverThread.start();
        }
        // run requests through client
        client();
    }

    /**
     * Communicates with the server group to determine location of files
     */
    public static void client(){
        try {
            Thread.sleep(1000);
            Scanner cmdin = new Scanner(System.in);
            Socket socket = new Socket(servers.get(0), 5000);
            // sends output to the socket
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            while(true) {
                System.out.print("Enter desired file name: ");
                String s = cmdin.nextLine();
                out.writeUTF(s);
                Thread.sleep(250);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
