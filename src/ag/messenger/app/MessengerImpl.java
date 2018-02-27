package ag.messenger.app;

import java.util.List;

import ag.messenger.model.Message;
import ag.messenger.model.Messenger;
import ag.messenger.model.SharedRepository;

public class MessengerImpl implements Messenger {
	private final SharedRepository repository;
	
	public MessengerImpl(SharedRepository repository) {
		this.repository = repository;
	}

	@Override
	public void send(Message m) {
		repository.store(m);
	}

	@Override
	public List<Message> receive(int id) {
		return repository.select(id);
	}

}
