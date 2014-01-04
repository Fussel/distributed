/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributed.net;

import distributed.dao.Post;
import distributed.dao.PrivateMessage;
import distributed.util.SettingsProvider;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextPane;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

/**
 *
 * @author steffen
 */
public class DistributedCore {

    private static final String LEADER_CHANNEL = "DistributedLeaders";

    private JChannel groupChannel;
    private JChannel leaderChannel;
    private String userName;
    private boolean isLeader;
    public final boolean moderator;
    private Address leader;

    private static DistributedCore instance;
    
    private List<String> userList; // only for testing
    
    private JTextPane output;

    public static DistributedCore getInstance() {
        if (instance == null) {
            instance = new DistributedCore();
        }

        return instance;
    }

    private DistributedCore() {
        moderator = false;

        try {
            //TODO register the listeners
            groupChannel = new JChannel();
            groupChannel.setReceiver(new GroupListener());
            groupChannel.setName(SettingsProvider.getInstance().getUserName());
            leaderChannel = new JChannel();
            //TODO Set the operator flag
            
            userList = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO Log the event
        }
    }

    /**
     * Join the given group cluster. If no one else is in the cluster, the
     * cluster would be opened and the this client will become the leader of the
     * cluster.
     *
     * @param groupName The name of the group cluster.
     * @return True if the client connected successfully, else false.
     */
    public boolean joinGroup(String groupName) {
        try {
            groupChannel.connect(groupName);
            bootStrap();

            //TODO Check other members
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            //TODO Log the evenet.
            return false;
        }
    }

    public void joinLeaderChannel() {
        try {
            leaderChannel.connect(userName);
        } catch (Exception e) {
            e.printStackTrace();
            //TODO Log dat shit.
        }
    }

    //TODO Determine if the PM and the posts have any difference in the sending process
    public boolean sendMessage(Message msg) {
        try {
            if (groupChannel != null && groupChannel.isConnected()) {
                groupChannel.send(msg);
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            //TODO Log dat shit
            return false;
        }
    }

    protected void userCheck(View view) {
        View chan = groupChannel.getView();

    }

    private void bootStrap() {
        System.out.println("Bootstrap started");
        View view = groupChannel.getView();
        if (view.getMembers().size() <= 1) {
            isLeader = true;
            try {
                leaderChannel.connect(LEADER_CHANNEL);
                System.out.println("LeaderChannel joined");
                output.setText(output.getText() + "\n" + "LeaderChannel joined");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            System.out.println("--------------------------------------");
            output.setText(output.getText() + "\n" + "LeaderChannel joined");
            for (Address a : view.getMembers()) {
                System.out.println(a.toString());
                output.setText(output.getText() + "\n" + a.toString());
            }
            System.out.println("--------------------------------------");
        }

        Address u = view.getMembers().get((int) (view.getMembers().size() * Math.random()));
        sendMessage(new Message(u, "update"));
        //TODO Start the update

    }

    public void setTextPanel(JTextPane jTextPaneMain) {
        if (jTextPaneMain != null) { 
            output = jTextPaneMain;
        }
    }

    private class GroupListener extends ReceiverAdapter {

        @Override
        public void receive(Message msg) {
            if (msg.getObject() instanceof Address) {
                System.out.println("Leader is: " + (Address) msg.getObject());
                output.setText(output.getText() + "\n" + "Leader is: " + (Address) msg.getObject());
            } else if (msg.getObject() instanceof String) {
                processStringMessage((String) msg.getObject());
            } else if(msg.getObject() instanceof Post) {
                
            } else if(msg.getObject() instanceof PrivateMessage) {
                
            }
        }

        private void processStringMessage(String message) {
            if (message.equals("update")) {
                System.out.println("Update request");
                output.setText(output.getText() + "\n" + "Update request");
            } else if(message.equals("close")) {
                groupChannel.close();
                System.out.println("Logged out");
                output.setText(output.getText() + "\n" + "Logged out");
            }
        }

        @Override
        public void viewAccepted(View view) {
            if (isLeader) {
                try {
                    //TODO Send leader message
                    System.out.println("***********************************");
                    System.out.println("User joined: " + view.getMembers().get(view.getMembers().size() - 1)); 
                    Address newUser = view.getMembers().get(view.getMembers().size() -1);
                    if(!userList.contains(newUser.toString())) {
                        userList.add(newUser.toString());
                        System.out.println("User accepted");
                    } else {
                        System.out.println("User already in network");
                        //TODO Longer actions should be moved to an own thread
                        sendMessage(new Message(newUser, "close"));
                        //TODO Close user connection. this could be done with JMX and the managed operation close()
                    }
                    System.out.println("***********************************");
                    groupChannel.send(new Message(null, groupChannel.getAddress()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            super.viewAccepted(view); //To change body of generated methods, choose Tools | Templates.
        }
    }

    private class LeaderListener extends ReceiverAdapter {

        @Override
        public void receive(Message msg) {
            super.receive(msg); 
            System.out.println("Leader message received");
        }

        @Override
        public void viewAccepted(View view) {
            super.viewAccepted(view); 
            System.out.println("New leader added");
        }
        
        
        
    }
}
