package com.mifish.bloomfilter.center.model;

import java.io.Serializable;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 13:46
 */
public class BloomFilterCommand implements Serializable {

    /***action*/
    private BloomFilterAction action;

    /***command*/
    private String command;

    /**
     * BloomFilterCommand
     *
     * @param action
     * @param command
     */
    public BloomFilterCommand(BloomFilterAction action, String command) {
        this.action = action;
        this.command = command;
    }

    public BloomFilterAction getAction() {
        return action;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BloomFilterCommand that = (BloomFilterCommand) o;

        if (action != that.action) {
            return false;
        }
        return command != null ? command.equals(that.command) : that.command == null;
    }

    @Override
    public int hashCode() {
        int result = action != null ? action.hashCode() : 0;
        result = 31 * result + (command != null ? command.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BloomFilterCommand{" +
                "action=" + action +
                ", command='" + command + '\'' +
                '}';
    }

    /**
     * Description:
     *
     * @author: rls
     * Date: 2017-10-15 13:46
     */
    public enum BloomFilterAction {

        FORCEBUILD,

        FORCELOAD
    }
}



