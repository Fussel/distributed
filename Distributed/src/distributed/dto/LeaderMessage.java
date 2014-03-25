/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributed.dto;

import distributed.util.SettingsProvider;
import java.io.Serializable;

/**
 *
 * @author kiefer.m
 */
public class LeaderMessage extends IMessage implements Serializable {

    public static final String TABLE_NAME = "leader_message";

    public LeaderMessage() {
        //needed by ORMLite
    }

    public LeaderMessage(String message) {
        super(SettingsProvider.getInstance().getUserName(), message.getBytes());
    }

    public String getMessageString() {
        return new String(message);
    }
}
