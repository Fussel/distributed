/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributed.net;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.blocks.GroupRequest;

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
            groupChannel.setName("Hans");
            leaderChannel = new JChannel();
            //TODO Set the operator flag
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            System.out.println("--------------------------------------");
            for (Address a : view.getMembers()) {
                System.out.println(a.toString());
            }
            System.out.println("--------------------------------------");

        }

        Address u = view.getMembers().get((int) (view.getMembers().size() * Math.random()));
        sendMessage(new Message(u, "update"));
        //TODO Start the update

    }

    private class GroupListener extends ReceiverAdapter {

        @Override
        public void receive(Message msg) {
            if (msg.getObject() instanceof Address) {
                System.out.println("Leader is: " + (Address) msg.getObject());
            } else if (msg.getObject() instanceof String) {
                processStringMessage((String) msg.getObject());
            }
        }

        private void processStringMessage(String message) {
            if (message.equals("update")) {
                System.out.println("Update request");
            }
        }

        @Override
        public void viewAccepted(View view) {
            if (isLeader) {
                try {
                    //TODO Send leader message
                    System.out.println("***********************************");
                    System.out.println(view);
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

    }
}
