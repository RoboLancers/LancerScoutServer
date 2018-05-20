package main.utility;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.util.ArrayList;

public class WaitThread implements Runnable{

    private ProcessConnectionThread processConnectionThread;
    private ArrayList<ProcessConnectionThread> connections;

    public WaitThread(){
        connections = new ArrayList<>();
    }

    @Override
    public void run() {
        waitForConnection();
    }

    private void waitForConnection(){
        LocalDevice local = null;

        StreamConnectionNotifier notifier;
        StreamConnection connection = null;

        try{
            local = LocalDevice.getLocalDevice();
            local.setDiscoverable(DiscoveryAgent.GIAC);

            UUID uuid = new UUID("fba199f747a74ed4b8803073424d2e2c", false);
            String url = "btspp://localhost:" + uuid.toString() + ";name=FRCScouting";
            notifier = (StreamConnectionNotifier) Connector.open(url);
        }catch(Exception e){
            e.printStackTrace();
            return;
        }

        while(true) {
            try {
                System.out.println("Waiting for connection... so lonely");
                connection = notifier.acceptAndOpen();

                processConnectionThread = new ProcessConnectionThread(connection);
                connections.add(processConnectionThread);

                Thread processThread = new Thread(processConnectionThread);
                processThread.start();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public ProcessConnectionThread getCurrentProcessConnectionThread() {
        return processConnectionThread;
    }

    public ArrayList<ProcessConnectionThread> getConnections(){
        return connections;
    }
}