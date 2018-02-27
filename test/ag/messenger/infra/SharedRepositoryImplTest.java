package ag.messenger.infra;

import java.util.Date;
import java.util.List;

import ag.messenger.model.Message;
import ag.messenger.model.SharedRepository;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;

public class SharedRepositoryImplTest {

	public void store(){
		//iniciar o repositório
		SharedRepository repository = new SharedRepositoryImpl();
		//criar a mensagem
		Message m = new Message();
		m.setId(1);
		m.setFrom("ari");
		m.setText("Primeira mensagem");
		m.setDate(new Date());
		//armazenar a mensagem
		repository.store(m);
		//
		m = new Message();
		m.setId(2);
		m.setFrom("ari");
		m.setText("Segunda mensagem");
		m.setDate(new Date());
		//armazenar a mensagem
		repository.store(m);
	}
        
	public void list1(){
		//iniciar o repositório
		SharedRepository repository = new SharedRepositoryImpl();
		List<Message> list = repository.select(1);
		assertEquals(1, list.size());
		assertEquals(2, list.get(0).getId());
	}
	
        public void list0(){
		//iniciar o repositório
		SharedRepository repository = new SharedRepositoryImpl();
		List<Message> list = repository.select(0);
		assertEquals(2, list.size());
		assertEquals(1, list.get(0).getId());
		assertEquals(2, list.get(1).getId());
	}
	
        @Test
	public void testAll(){
		store();
		list0();
		list1();
	}
	
}
