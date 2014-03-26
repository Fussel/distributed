/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributed.net;

import distributed.dto.GroupMessage;
import distributed.dto.LeaderMessage;
import distributed.dto.PrivateMessage;
import distributed.util.SettingsProvider;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.JTextPane;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.protocols.BARRIER;
import org.jgroups.protocols.FD_ALL;
import org.jgroups.protocols.FD_SOCK;
import org.jgroups.protocols.FRAG2;
import org.jgroups.protocols.MERGE2;
import org.jgroups.protocols.MFC;
import org.jgroups.protocols.PING;
import org.jgroups.protocols.UDP;
import org.jgroups.protocols.UFC;
import org.jgroups.protocols.UNICAST2;
import org.jgroups.protocols.VERIFY_SUSPECT;
import org.jgroups.protocols.pbcast.GMS;
import org.jgroups.protocols.pbcast.NAKACK;
import org.jgroups.stack.ProtocolStack;

/**
 *
 * @author steffen
 */
public class DistributedCore {

    private static final String LEADER_CHANNEL = "DistributedLeaders";

    private JChannel groupChannel;
    private JChannel leaderChannel;
    private String userName;
    public boolean isLeader;
    public final boolean moderator;
    private Address leader;

    private static DistributedCore instance;

    private ProtocolStack stack;
    ProtocolStack mLeaderStack;
    private InetAddress networkInterface;

    private List<String> userList; // only for testing

    public static DistributedCore getInstance() {
        if (instance == null) {
            instance = new DistributedCore();
        }

        return instance;
    }

    public String getUserName() {
        return userName;
    }

    private DistributedCore() {
        moderator = false;

        try {
            userName = SettingsProvider.getInstance().getUserName();
            //TODO register the listeners
            groupChannel = new JChannel(false);
            stack = new ProtocolStack();

            groupChannel.setProtocolStack(stack);
            groupChannel.setReceiver(new GroupListener());
            groupChannel.setName(SettingsProvider.getInstance().getUserName() + "-" + Math.random());

            //leaderChannel = new JChannel();
            //TODO Set the operator flag
            userList = new ArrayList<>();

            mLeaderStack = new ProtocolStack();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO Log the event
        }

        if (leaderChannel == null) {
            System.out.println("TEST");
            leaderChannel = new JChannel(false);
            mLeaderStack = new ProtocolStack();
            leaderChannel.setProtocolStack(mLeaderStack);
            leaderChannel.setReceiver(new LeaderListener());
            //leaderChannel.setName(SettingsProvider.getInstance().getUserName() + "-" + Math.random());
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
            System.out.println("Group-Channel joined: " + groupChannel.getClusterName());
            System.out.println(groupChannel.getViewAsString());

            bootStrap();

            //TODO Check other members
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            //TODO Log the evenet.
            return false;
        }
    }

    /**
     * This method configures the protocol stack of the DistributedCore and must
     * be run before you can use the instance of DistributedCore.
     *
     * @param interfaceAddress The InetAddress of the network interface which
     * should be used to communicate with this
     */
    public void configure(InetAddress interfaceAddress) {
        if (interfaceAddress == null) {
            throw new IllegalArgumentException();
        }

        networkInterface = interfaceAddress;

        try {
            instance.buildigProtocollStack(stack);
            instance.buildigProtocollStack(mLeaderStack);
//        } catch(UnconfiguredException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Building the protocol stack.
     *
     * @throws UnconfiguredException
     */
    private void buildigProtocollStack(ProtocolStack s) throws RuntimeException {

        if (networkInterface == null) {
            throw new RuntimeException(); // Wird noch geändert
        }
        s.addProtocol(new UDP().setValue("bind_addr", networkInterface))
                .addProtocol(new PING())
                .addProtocol(new MERGE2())
                .addProtocol(new FD_SOCK())
                .addProtocol(new FD_ALL().setValue("timeout", 12000)
                        .setValue("interval", 3000))
                .addProtocol(new VERIFY_SUSPECT())
                .addProtocol(new BARRIER())
                .addProtocol(new NAKACK())
                .addProtocol(new UNICAST2())
                //.addProtocol(new STABLE())
                .addProtocol(new GMS())
                .addProtocol(new UFC())
                .addProtocol(new MFC())
                .addProtocol(new FRAG2());
        try {
            s.init();
        } catch (Exception ex) {
            Logger.getLogger(DistributedCore.class.getName()).log(Level.SEVERE, "Unable to init networkstack", ex);
        }
    }

    protected void joinLeaderChannel() {
        try {
            leaderChannel.connect(LEADER_CHANNEL);
        } catch (Exception ex) {
            Logger.getLogger(DistributedCore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //TODO Determine if the PM and the posts have any difference in the sending process
    public boolean sendMessage(Message msg) {
        try {

            if (groupChannel != null && groupChannel.isConnected() && !(msg.getObject() instanceof LeaderMessage)) {
                groupChannel.send(msg);
                return true;
            } else if (leaderChannel != null && leaderChannel.isConnected() && (msg.getObject() instanceof LeaderMessage)) {
                leaderChannel.send(msg);
                return true;
            } else {
                return false;
            }

        } catch (Exception ex) {
            Logger.getLogger(DistributedCore.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    protected void userCheck(View view) {
        View chan = groupChannel.getView();
    }

    /**
     * This method is used for a forced logout of this client and is triggered
     * when there was a problem while logging in.
     */
    protected void closeConnection() {
        closeGroupConnection();
        closeLeaderConnection();
    }

    protected void closeLeaderConnection() {
        if (leaderChannel != null) {
            leaderChannel.close();
        }
    }

    protected void closeGroupConnection() {
        if (groupChannel != null) {
            groupChannel.close();
        }
    }

    public void disconnectLeaderChannel() {

        leaderChannel.disconnect();
        //bootStrap();
        View view = groupChannel.getView();
        if (view.getMembers().size() > 1) {
            try {
                leaderChannel.connect(LEADER_CHANNEL, view.getMembers().get(view.getMembers().size() - 1), (long) 0.0);
            } catch (Exception ex) {
                Logger.getLogger(DistributedCore.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            groupChannel.close();
        }
    }

    private void bootStrap() {
        System.out.println("Bootstrap started");
        View view = groupChannel.getView();

        System.out.println("Creator: " + view.getCreator());
        //TODO 
        if (view.getMembers().size() <= 1) {

            isLeader = true;
            joinLeaderChannel();
            System.out.println("Channel joined: " + leaderChannel.getClusterName());
            //
            SettingsProvider.getInstance().storeUserType(SettingsProvider.UserType.MODERATOR);
        } else {
            System.out.println("--------------------------------------");

            for (Address a : view.getMembers()) {
                System.out.println(a.toString());

            }
            System.out.println("--------------------------------------");
        }

        //Address u = view.getMembers().get((int) (view.getMembers().size() * Math.random()));
        //sendMessage(new Message(u, "update"));
        //TODO Start the update
    }

    public void disconnectGroupChannelChannel() {
       groupChannel.disconnect();
       
       if (groupChannel.getView().getMembers().isEmpty()) {
           closeGroupConnection();
       }
    }

    private class GroupListener extends ReceiverAdapter {

        @Override
        public void receive(Message msg) {
            MessageProcessor.getInstance().addMessage(msg);
        }

        @Override
        public void viewAccepted(View view) {
            if (isLeader) {
                try {
                    //TODO Send leader message
                    System.out.println("***********************************");
                    System.out.println("User joined: " + view.getMembers().get(view.getMembers().size() - 1));
                    Address newUser = view.getMembers().get(view.getMembers().size() - 1);
                    if (!userList.contains(newUser.toString())) {
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
            MessageProcessor.getInstance().addMessage(msg);
            System.out.println("Leader message received");
        }

        @Override
        public void viewAccepted(View view) {
            super.viewAccepted(view);
            System.out.println("New leader added");
        }

    }
}
