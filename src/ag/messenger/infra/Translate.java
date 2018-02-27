package ag.messenger.infra;

import ag.messenger.model.Message;

public interface Translate {
	String toJSON(Message m);
	Message fromJSON(String json);
}
