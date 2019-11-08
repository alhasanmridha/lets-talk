package org.appspot.apprtc.signalling;

public class SignalManager {


    public void init(){
        Connection connection = new Connection("test3", "mist");
        connection.execute();
    }

    public static void main(String[] args){
        System.out.println("There it is");
    }
}
