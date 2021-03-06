package distributed.main;

import distributed.database.DatabaseManager;
import distributed.dto.GroupMessage;
import distributed.dto.IMessage;
import distributed.dto.LeaderMessage;
import distributed.dto.PrivateMessage;
import distributed.msg.MsgDialog;
import distributed.net.DistributedCore;
import distributed.net.Messenger;
import distributed.net.Messenger.MessageCallback;
import distributed.settings.SettingsDialog;
import distributed.user.AccessFrame;
import distributed.user.ChangeMessageDialog;
import distributed.util.DateUtils;
import distributed.util.DistributedKrypto;
import distributed.util.SettingsProvider;
import distributed.util.SettingsProvider.UserType;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author kiefer
 */
public class MainFrame extends javax.swing.JFrame implements MessageCallback {

    private boolean contextMenu = true;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();

        //Starte Programm
       

        if (SettingsProvider.getInstance().getUserInterface() != null) {
            try {
                DistributedCore.getInstance().configure(InetAddress.getByName(SettingsProvider.getInstance().getUserInterface()));
            } catch (UnknownHostException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!SettingsProvider.getInstance().getUserGroup().equals("")) {
            DistributedCore.getInstance().joinGroup(SettingsProvider.getInstance().getUserGroup());
        }

        jList1.setModel(
                new MyListModel(null));
        jList1.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mousePressed(MouseEvent e
                    ) {
                        showPopup(e);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e
                    ) {
                        showPopup(e);
                    }

                    private void showPopup(MouseEvent e) {
                        if (e.isPopupTrigger() && contextMenu) {
                            showMenu(e);
                        }
                    }
                }
        );

        jLabel1.setText("Hello " + SettingsProvider.getInstance().getUserName() + "!");
        Messenger.getInstance().addMessageListener(this);
    }

    public void showMenu(MouseEvent evt) {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem jMenuItemShare = new javax.swing.JMenuItem();
        jMenuItemShare.setText("Share");
        IMessage m = ((MyListModel) jList1.getModel()).getMessageAt(jList1.getSelectedIndex());
        if (m.getSender().equals(SettingsProvider.getInstance().getUserName()) || SettingsProvider.getInstance().getUserType() == UserType.Moderator) {
            jMenuItemShare.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (jList1.getSelectedIndex() > -1) {
                        showMessageDialog(null, ((MyListModel) jList1.getModel()).getMessageAt(jList1.getSelectedIndex()));
                    }
                }
            });
        }
        JMenuItem jMenuItemDirectMessage = new javax.swing.JMenuItem();
        jMenuItemDirectMessage.setText("Direct Message");
        jMenuItemDirectMessage.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (jList1.getSelectedIndex() > -1) {
                    IMessage m = ((MyListModel) jList1.getModel()).getMessageAt(jList1.getSelectedIndex());
                    if (m instanceof GroupMessage) {
                        showMessageDialog(((GroupMessage) m).getSender(), m);
                    }
                }
            }

        });

        JMenuItem jMenuItemDeleteMessage = new javax.swing.JMenuItem();
        jMenuItemDeleteMessage.setText("Delete Message");
        jMenuItemDeleteMessage.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("delete message");
                if (jList1.getSelectedIndex() > -1) {
                    IMessage m = ((MyListModel) jList1.getModel()).getMessageAt(jList1.getSelectedIndex());
                    if (m.getSender().equals(SettingsProvider.getInstance().getUserName()) || SettingsProvider.getInstance().getUserType() == UserType.Moderator) {
                        DatabaseManager.getInstance().markAsDeleted((GroupMessage) m);
                        MyListModel model = (MyListModel) jList1.getModel();
                        model.remove(jList1.getSelectedIndex());
                        jList1.revalidate();
                        jList1.repaint();
                    }
                }
            }

        });
        JMenuItem jMenuItemChangeMessage = new javax.swing.JMenuItem();
        jMenuItemChangeMessage.setText("Change Message");
        jMenuItemChangeMessage.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("change message");
                //TODO ABfrage ob der user rechte hat
                if (jList1.getSelectedIndex() > -1) {
                    IMessage m = ((MyListModel) jList1.getModel()).getMessageAt(jList1.getSelectedIndex());
                    if (m.getSender().equals(SettingsProvider.getInstance().getUserName())) {
                        System.out.println("ich bin der user: valid");
                        Frame ChangeMessageDialog = null;
                        ChangeMessageDialog changeDialog = new ChangeMessageDialog(MainFrame.this, true);
                        changeDialog.setText(m); //set jtextarea in the dialog
                        changeDialog.setVisible(true);
                        MyListModel model = (MyListModel) jList1.getModel();
                        model.changeMsg(jList1.getSelectedIndex(), changeDialog.getText());
                        jList1.revalidate();
                        jList1.repaint();
                    }
                }

            }

        });

        menu.add(jMenuItemChangeMessage);
        menu.add(new JPopupMenu.Separator());
        menu.add(jMenuItemDeleteMessage);
        menu.add(new JPopupMenu.Separator());
        menu.add(jMenuItemDirectMessage);
        menu.add(new JPopupMenu.Separator());
        menu.add(jMenuItemShare);

        menu.show(evt.getComponent(), evt.getX(), evt.getY());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jButtonNewMsg = new javax.swing.JButton();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        jButtonAbout = new javax.swing.JButton();
        jButtonSettings = new javax.swing.JButton();
        jButtonLogout = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Statusleiste");

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButtonNewMsg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/distributed/icons/Mail_32x32.png"))); // NOI18N
        jButtonNewMsg.setText("New Message");
        jButtonNewMsg.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButtonNewMsg.setBorderPainted(false);
        jButtonNewMsg.setFocusable(false);
        jButtonNewMsg.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonNewMsg.setMinimumSize(new java.awt.Dimension(100, 50));
        jButtonNewMsg.setPreferredSize(new java.awt.Dimension(75, 50));
        jButtonNewMsg.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonNewMsg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonNewMsgMouseClicked(evt);
            }
        });
        jButtonNewMsg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewMsgActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonNewMsg);
        jToolBar1.add(filler3);

        jButtonAbout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/distributed/icons/Information_32x32.png"))); // NOI18N
        jButtonAbout.setText("About");
        jButtonAbout.setBorderPainted(false);
        jButtonAbout.setFocusable(false);
        jButtonAbout.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonAbout.setMinimumSize(new java.awt.Dimension(100, 50));
        jButtonAbout.setPreferredSize(new java.awt.Dimension(75, 50));
        jButtonAbout.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAboutActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonAbout);

        jButtonSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/distributed/icons/Options_32x32.png"))); // NOI18N
        jButtonSettings.setText("Settings");
        jButtonSettings.setBorderPainted(false);
        jButtonSettings.setFocusable(false);
        jButtonSettings.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSettings.setMinimumSize(new java.awt.Dimension(100, 50));
        jButtonSettings.setPreferredSize(new java.awt.Dimension(75, 50));
        jButtonSettings.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSettingsActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonSettings);

        jButtonLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/distributed/icons/Link_32x32.png"))); // NOI18N
        jButtonLogout.setText("Logout");
        jButtonLogout.setBorderPainted(false);
        jButtonLogout.setFocusable(false);
        jButtonLogout.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonLogout.setMinimumSize(new java.awt.Dimension(100, 50));
        jButtonLogout.setPreferredSize(new java.awt.Dimension(75, 50));
        jButtonLogout.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonLogoutMouseClicked(evt);
            }
        });
        jButtonLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLogoutActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonLogout);

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane2)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonLogoutMouseClicked

    }//GEN-LAST:event_jButtonLogoutMouseClicked

    private void jButtonNewMsgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonNewMsgMouseClicked

    }//GEN-LAST:event_jButtonNewMsgMouseClicked

    private void jButtonNewMsgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewMsgActionPerformed
        showMessageDialog(null, null);
    }//GEN-LAST:event_jButtonNewMsgActionPerformed


    private void jButtonLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogoutActionPerformed
        this.setVisible(false);

        if (SettingsProvider.getInstance().getUserType() == SettingsProvider.UserType.MODERATOR) {
            DistributedCore.getInstance().isLeader = false;
            DistributedCore.getInstance().disconnectLeaderChannel();
            SettingsProvider.getInstance().storeUserType(SettingsProvider.UserType.USER);
        }

        AccessFrame mAccessFrame = new AccessFrame();
        mAccessFrame.setLocationRelativeTo(this);
        mAccessFrame.setVisible(true);
    }//GEN-LAST:event_jButtonLogoutActionPerformed

    private void jButtonSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSettingsActionPerformed
        JFrame rootWindow = (JFrame) SwingUtilities.getWindowAncestor(this);
        SettingsDialog dialog = new SettingsDialog(rootWindow, true);
        dialog.setTitle("Settings");
        dialog.setLocationRelativeTo(rootWindow);
        dialog.setVisible(true);
    }//GEN-LAST:event_jButtonSettingsActionPerformed

    private void jButtonAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAboutActionPerformed
        JFrame rootWindow = (JFrame) SwingUtilities.getWindowAncestor(this);
        AboutDialog dialog = new AboutDialog(rootWindow, true);
        dialog.setTitle("About");
        dialog.setLocationRelativeTo(rootWindow);
        dialog.setVisible(true);
    }//GEN-LAST:event_jButtonAboutActionPerformed

    private void showMessageDialog(String reciever, IMessage message) {
        JFrame rootWindow = (JFrame) SwingUtilities.getWindowAncestor(this);
        MsgDialog dialog = new MsgDialog(rootWindow, true, message, reciever);

        if (message != null && reciever == null) {
            dialog.setTitle("Share Message");
        } else {
            dialog.setTitle("New Message");
        }

        dialog.setLocationRelativeTo(rootWindow);
        dialog.setVisible(true);

        IMessage m = dialog.getMessage();
        if (m != null) {
            if (m instanceof PrivateMessage) {
                Messenger.getInstance().sendMessage((PrivateMessage) m);
            } else if (m instanceof GroupMessage) {
                Messenger.getInstance().sendGroupMessage((GroupMessage) m);
            } else if (m instanceof LeaderMessage) {
                Messenger.getInstance().sendLeaderMessage((LeaderMessage) m);
            } else {
                System.out.println("Nachricht is null");
            }
        } else {
            System.out.println("Keine Nachricht übergeben.");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            /*for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
             if ("Nimbus".equals(info.getName())) {
             javax.swing.UIManager.setLookAndFeel(info.getClassName());
             break;

             }
             }*/
            javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    @Override
    public void messageReceived(IMessage msg) {
        ((MyListModel) jList1.getModel()).addElement(msg);
    }

    class MyListModel extends AbstractListModel {

        private final ArrayList<IMessage> messages;

        public MyListModel(ArrayList<IMessage> messages) {
            this.messages = new ArrayList<>();
        }

        @Override
        public int getSize() {
            return messages.size();
        }

        @Override
        public String getElementAt(int index) {
            IMessage m = messages.get(index);
            //TODO Return the object
            StringBuilder sb = new StringBuilder();
            if (m instanceof GroupMessage) {
                GroupMessage gm = ((GroupMessage) messages.get(index));
                sb.append("[").append(DateUtils.getTimeFormatAsString(gm.getSendDate())).append("]");
                sb.append("von ").append(gm.getSender()).append(": ");
                sb.append(new String(gm.getMessage()));
                return sb.toString();
            } else if (m instanceof PrivateMessage) {
                PrivateMessage pm = ((PrivateMessage) messages.get(index));
                sb.append("[").append(DateUtils.getTimeFormatAsString(pm.getSendDate())).append("]");
                sb.append("von ").append(pm.getSender()).append(": ");
                sb.append(DistributedKrypto.getInstance().decryptMessage(pm.getMessage())).append(" (privat)");
                return sb.toString();
            } else if ((m instanceof LeaderMessage) && (SettingsProvider.getInstance().getUserType() == SettingsProvider.UserType.MODERATOR)) {
                LeaderMessage lm = ((LeaderMessage) messages.get(index));
                sb.append("[").append(DateUtils.getTimeFormatAsString(lm.getSendDate())).append("]");
                sb.append("von ").append(lm.getSender()).append(": ");
                sb.append(new String(lm.getMessage())).append(" (leader)");
                return sb.toString();
            }

            return null;
        }

        public void addElement(IMessage m) {
            messages.add(m);
            this.fireContentsChanged(this, this.getSize(), messages.size());
        }

        public IMessage getMessageAt(int index) {
            return (IMessage) messages.get(index);
        }

        public void clear() {
            messages.clear();
        }

        public ArrayList<IMessage> getMessages() {
            return messages;
        }

        public void remove(int position) {
            messages.remove(position);

        }

        public void changeMsg(int position, String newMsg) {
            IMessage msg = messages.get(position);
            msg.setMessage(newMsg.getBytes());
        }

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler3;
    private javax.swing.JButton jButtonAbout;
    private javax.swing.JButton jButtonLogout;
    private javax.swing.JButton jButtonNewMsg;
    private javax.swing.JButton jButtonSettings;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
