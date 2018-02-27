package ag.messenger.app;

import java.util.Scanner;
import ag.messenger.infra.SharedRepositoryImpl;
import ag.messenger.model.Messenger;
import ag.messenger.model.SharedRepository;

public class Main {

    public static void main(String[] args) {
        //
        SharedRepository repository = new SharedRepositoryImpl();
        Messenger messenger = new MessengerImpl(repository);
        //
        Scanner scanner = new Scanner(System.in);
        //fazer login
        System.out.println("Por favor, informe seu nome:");
        String name = scanner.nextLine();
        //
        System.out.println();
        //
        System.out.println("Chat");
        System.out.println("-----------------------------");
        //
        Thread read = new Thread(new UserReader(repository, name));
        Thread write = new Thread(new UserWriter(repository, name));
        read.start();
        write.start();
    }
}
