/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag.messenger.app;

import ag.messenger.model.Message;
import ag.messenger.model.SharedRepository;
import java.util.List;

/**
 *
 * @author juan
 */
public class UserReader implements Runnable {

    private SharedRepository repository;
    private String name;
    private int rid = 0;

    public UserReader(SharedRepository repository, String name) {
        this.repository = repository;
        this.name = name;
    }

    @Override
    public void run() {
        while (true) {
            //
            List<Message> list = repository.select(rid);
            for (Message message : list) {
                //diferenciar por nome
                if (!message.getFrom().equals(name)) {
                    System.out.println(String.format("%s: %s",
                            message.getFrom(), message.getText()));
                }
                //
                rid = message.getId();
            }
        }
    }

}
