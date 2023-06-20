package pt.isec.pa.apoio_poe.model.data.command;

import pt.isec.pa.apoio_poe.model.data.Data;

import java.io.Serializable;

abstract class CommandAdapter implements ICommand, Serializable {
    protected Data data;

    public CommandAdapter(Data data){this.data = data;}

}
