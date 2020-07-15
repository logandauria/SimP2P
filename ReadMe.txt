I implemeted a SimP2P system for file sharing.

My implementation of SimP2P was using Java socket programming, having the servers
run on threads. The program requires a hostfile containing 4 IP addresses.

The program has a client connected to the server cluster that prompts the user
to search for a file. Then the file location is either printed or is not
found. Each server creates their own files to contribute to the head server's
data structure.
