// Server implementation for P2P Simulation. It is assumed that there are 4 servers.
// One head server and 3 participating servers. Each server creates 5 files and shares
// them with the head server. These files are stored in a hashmap and can be requested
// through the client in the main program.
//
// @author: Logan D'Auria
// @email:  lxd1644@rit.edu

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class server implements Runnable{

    private int id;
    private String ip;
    private int port;
    private String head;
    private Socket socket = null;

    private HashMap<String, file> filestorage;



    /**
     * The {@link ServerSocket} used to wait for incoming connections.
     */
    private ServerSocket serv;

    private ArrayList<file> files = new ArrayList<>();

    public server(int id, String ip, int port, String head) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.head = head;
        try {
            this.serv = new ServerSocket(port);
        } catch (IOException e) {
        }
    }

    /**
     * Requests a file from the head node's location storage
     * @param filename: name of desired file
     * @return: desired file's location or "File Not Found"
     */
    public String request(String filename) throws IOException {
        DataInputStream input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        DataOutputStream output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        output.writeUTF(filename);
        String response = input.readUTF();
        if(response.equals("File Not Found")){
            return "File Not Found";
        } else {
            return response;
        }
    }

    public void run() {
        try {
            if (id == 0) { // Head server node functionality

                // Data structure to hold all file locations
                filestorage = new HashMap<>(50);

                // Server cap at 4, so head node has 3 peers, needing 3 sockets and input streams
                Socket[] peers = new Socket[3];
                ObjectInputStream[] inputs = new ObjectInputStream[3];
                ObjectOutputStream[] outputs = new ObjectOutputStream[3];
                // Connect socket and initialize input stream
                for(int x = 0; x < 3; x++){
                    peers[x] = serv.accept();
                    inputs[x] = new ObjectInputStream(new BufferedInputStream(peers[x].getInputStream()));
                    outputs[x] = new ObjectOutputStream(new BufferedOutputStream(peers[x].getOutputStream()));
                }
                file f;
                // Read all files from peers and add them to hashmap
                for(ObjectInputStream input : inputs) {
                    for (int x = 0; x < 5; x++) {
                        f = (file) input.readObject();
                        filestorage.put(f.name, f);
                    }
                }

                // Print out all files and their information
                System.out.println("List of all files in format:\nFILENAME, IP ADDRESS, PORT\n");
                List<String> keys = new ArrayList<>(filestorage.keySet());
                for(String key : keys){
                    System.out.println(filestorage.get(key));
                }
                System.out.println();

                // Keeps server running while waiting for requests.
                Socket s = serv.accept();
                DataInputStream data = new DataInputStream(new BufferedInputStream(s.getInputStream()));
                while(true){
                    String str = data.readUTF();
                    if(filestorage.keySet().contains(str)){
                        System.out.println(filestorage.get(str));
                    } else {
                        System.out.println("File Not Found");
                    }
                }

            } else { // Requester server node functionality
                file[] files = new file[5];
                for(int x = 0; x < 5; x++){
                    files[x] = new file("file" + id + x, ip, port);
                }

                socket = new Socket(head, start.PORT_NUMBER);
                // sends output to the socket
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                // writes all of its files to the head server
                for(file f : files){
                    out.writeObject(f);
                }

                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}