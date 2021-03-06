/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributed.msg;

import distributed.dto.GroupMessage;
import distributed.dto.IMessage;
import distributed.dto.LeaderMessage;
import distributed.dto.PrivateMessage;
import distributed.net.DistributedCore;
import distributed.net.Messenger;
import distributed.util.DistributedKrypto;
import distributed.util.MessageHelper;
import distributed.util.SettingsProvider;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jgroups.Message;

/**
 *
 * @author kiefer
 */
public class MsgDialog extends javax.swing.JDialog {

    private IMessage mMessage;
    private String reciever;

    public MsgDialog(JFrame rootWindow, boolean b, IMessage message, String reciever) {
        super(rootWindow, b);
        initComponents();

        mMessage = message;

        if (message != null && (message instanceof GroupMessage)) {
            jTextFieldMsg.setText(new String(message.getMessage()));
            jComboBoxGroup.setEnabled(true);
        }

        if (reciever != null) {
            this.reciever = reciever;
            jLabelUser.setText(reciever);
            jTextFieldMsg.setText("");
            jComboBoxGroup.setEnabled(false);
        }

        if (SettingsProvider.getInstance().getUserType() == SettingsProvider.UserType.MODERATOR) {
            jCheckBoxLeaderGroup.setVisible(true);
        } else {
            jCheckBoxLeaderGroup.setVisible(false);
        }

    }

    public IMessage getMessage() {
        return mMessage;
    }

    public void setMessage(IMessage m) {
        this.mMessage = m;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonOk = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jLabelGroup = new javax.swing.JLabel();
        jComboBoxGroup = new javax.swing.JComboBox();
        jLabelMsg = new javax.swing.JLabel();
        jTextFieldMsg = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabelUser = new javax.swing.JLabel();
        jCheckBoxLeaderGroup = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jButtonOk.setText("Send");
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });
        jButtonOk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButtonOkKeyPressed(evt);
            }
        });

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jLabelGroup.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelGroup.setText("to group:");

        jComboBoxGroup.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Gruppe HTW", "Gruppe PI", "Gruppe DEA", "Gruppe Andreas" }));
        jComboBoxGroup.setEnabled(false);

        jLabelMsg.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelMsg.setText("message:");

        jTextFieldMsg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldMsgActionPerformed(evt);
            }
        });
        jTextFieldMsg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldMsgKeyPressed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("to user:");

        jLabelUser.setText("all");

        jCheckBoxLeaderGroup.setText("Leadergroup");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOk))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelGroup)
                            .addComponent(jLabelMsg, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldMsg)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelUser)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jComboBoxGroup, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckBoxLeaderGroup)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelMsg)
                    .addComponent(jTextFieldMsg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelGroup)
                    .addComponent(jComboBoxGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxLeaderGroup))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabelUser))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonOk)
                    .addComponent(jButtonCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        mMessage = null;
        this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed

        if (jTextFieldMsg.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this.getParent(), "Message is empty.");
            jTextFieldMsg.requestFocus();
        } else {
            if (reciever != null) {
                if (mMessage instanceof GroupMessage) {
                    Message m = MessageHelper.buildPrivateMessage((GroupMessage) mMessage, jTextFieldMsg.getText());
                    DistributedCore.getInstance().sendMessage(m);
                }
                mMessage = null;
                //mMessage = new PrivateMessage(reciever, DistributedKrypto.getInstance().encryptString(jTextFieldMsg.getText(), DistributedKrypto.getInstance().getMyPublicKey()));
            } else if ((SettingsProvider.getInstance().getUserType() == SettingsProvider.UserType.MODERATOR) && (jCheckBoxLeaderGroup.isSelected())) {
                mMessage = new LeaderMessage(jTextFieldMsg.getText());
            } else {
                mMessage = new GroupMessage(DistributedKrypto.getInstance().publicKeyToString(DistributedKrypto.getInstance().getMyPublicKey()),
                        jTextFieldMsg.getText());
            }

            this.dispose();
        }

    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonOkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonOkKeyPressed

    }//GEN-LAST:event_jButtonOkKeyPressed

    private void jTextFieldMsgKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldMsgKeyPressed
        if ((int) evt.getKeyChar() == 10) {
            jButtonOk.doClick();
        }
    }//GEN-LAST:event_jTextFieldMsgKeyPressed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed

    }//GEN-LAST:event_formKeyPressed

    private void jTextFieldMsgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldMsgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldMsgActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JCheckBox jCheckBoxLeaderGroup;
    private javax.swing.JComboBox jComboBoxGroup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelGroup;
    private javax.swing.JLabel jLabelMsg;
    private javax.swing.JLabel jLabelUser;
    private javax.swing.JTextField jTextFieldMsg;
    // End of variables declaration//GEN-END:variables
}
