package pt.isec.pa.apoio_poe.model.data.memento;

import java.io.*;

public class Memento implements IMemento, Serializable {
    byte[] snapshot;
    public Memento(Object obj) {
        try (ByteArrayOutputStream baos =
                     new ByteArrayOutputStream();
             ObjectOutputStream oos =
                     new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            snapshot = baos.toByteArray();
        } catch (Exception e) { snapshot = null; }
    }

    @Override
    public Object getSnapshot() {
        if (snapshot == null) return null;
        try (ByteArrayInputStream bais =
                     new ByteArrayInputStream(snapshot);
             ObjectInputStream ois =
                     new ObjectInputStream(bais)) {
            return ois.readObject();
        } catch (Exception e) { return null; }
    }
}
