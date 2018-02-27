/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag.messenger.app;

import ag.messenger.model.Message;
import ag.messenger.model.SharedRepository;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author juan
 */
public class UserWriter implements Runnable {

    private SharedRepository repository;
    private String name;
    private int wid;

    public UserWriter(SharedRepository repository, String name) {
        this.repository = repository;
        this.name = name;
    }

    @Override
    public void run() {
        while (true) {
            //
            Scanner scanner = new Scanner(System.in);
            String msg = scanner.nextLine();
            //
            setWid(wid);
            //
            Message m = new Message();
            m.setId(wid);
            m.setFrom(name);
            m.setText(msg);
            m.setDate(new Date());
            //
            repository.store(m);
        }
    }

    private void setWid(int id){
        this.wid = this.repository.getGlobalId() + 1;
    }
}
