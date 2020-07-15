// file representation for SimP2P
//
// @author: Logan D'Auria
// @email:  lxd1644@rit.edu
import java.io.Serializable;

public class file implements Serializable {

    public String name;
    public String ip;
    public int port;

    public file(String name, String ip, int port){
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String toString(){
        return name + ", " + ip + ", " + port;
    }
}
