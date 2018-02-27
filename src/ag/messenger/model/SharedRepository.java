package ag.messenger.model;

import java.io.File;
import java.util.List;

public interface SharedRepository {
	void store(Message m);
	List<Message> select(int id);
        int getGlobalId();
        void setGlobalId(int id);
        void setIdToFile(File file, int id);
}
