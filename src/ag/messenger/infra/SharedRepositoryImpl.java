package ag.messenger.infra;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ag.messenger.model.Message;
import ag.messenger.model.SharedRepository;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class SharedRepositoryImpl implements SharedRepository {

    private final static String PATH = "rep/data.txt";
    private final Translate translate;
    private int globalId = 1;
    private UUID id = UUID.randomUUID();

    public SharedRepositoryImpl() {
        translate = new TranslateImpl();
    }

    @Override
    public void store(Message m) {
        try {
            if (Paths.get(PATH).toFile().exists()) {

                //renomeia o arquivo para o <key>.lock
                Files.move(Paths.get(PATH), Paths.get(PATH).resolveSibling(id + ".lock"));
                //inicia a rotina de escrita no arquivo
                File file = new File("rep/" + id + ".lock");
                FileOutputStream out = new FileOutputStream(file, true);
                out.write(translate.toJSON(m).getBytes());
                out.write("\n".getBytes());
                //encerra escrita
                out.close();
                //alterar o último id na primeira linha do arquivo
                setIdToFile(file, m.getId());
                //renomeia o arquivo de volta para o arquivo txt inicial
                Files.move(Paths.get("rep/" + id + ".lock"), Paths.get("rep/" + id + ".lock").resolveSibling("data.txt"));
            } else {
                Thread.sleep(100);
                store(m);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<Message> select(int id) {
        try {
            File file = new File(PATH);
            if (!file.exists()) {
                return new ArrayList<Message>();
            }
            /*
            Problema ? : select pode chegar aqui enquanto o write estiver escrevendo;
            uma exceção (No such file or directory) pode ser lançada
             */
            FileInputStream input = new FileInputStream(file);
            //
            List<String> lines = new ArrayList<String>();
            StringBuffer sb = new StringBuffer();
            while (true) {
                byte[] b = new byte[1];
                //
                int r = input.read(b);
                if (r == -1) {
                    break;
                }
                if (r == 0) {
                    continue;
                }
                //
                String charact = new String(b);
                if (charact.equals("\n")) {
                    lines.add(sb.toString());
                    sb = new StringBuffer();
                } else {
                    sb.append(charact);
                }
            }
            //
            input.close();
            //
            List<Message> list = new ArrayList<Message>();
            for (String l : lines) {
                //para não capturar o id como mensagem
                if (l.startsWith("#")) {
                    continue;
                }
                Message m = translate.fromJSON(l);
                if (m.getId() > id) {
                    list.add(m);
                }
            }
            //
            return list;

        } catch (FileNotFoundException ex) {
            try {
                //solução temporária para o problema citado acima
                Thread.sleep(100);
                return select(id);
            } catch (InterruptedException ex1) {
                ex1.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public int getGlobalId() {
        FileInputStream fis = null;
        try {
            //lendo a primeira linha do arquivo            
            fis = new FileInputStream(PATH);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            //
            String id = reader.readLine();
            //
            if (id != null) {
                System.out.println("requesting id ->" + id.split("#")[1]);
                //convertendo o id encontrado em inteiro
                return Integer.parseInt(id.split("#")[1]);
            } else {
                System.out.println("requesting id -> 0");
                return 0;
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    @Override
    public void setGlobalId(int id) {
        this.globalId = id;
    }

    @Override
    public void setIdToFile(File file, int id) {
        try {
            //stream de leitura para o arquivo
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            //lê todas as linhas, menos o id
            StringBuilder sb = new StringBuilder();
            String line = new String();
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("#")) {
                    sb.append(line).append("\n");
                }
            }
            //
            String oldFile = sb.toString();
            //resetando builder
            sb.setLength(0);
            //adiciona o novo id como a primeira linha do arquivo
            sb.append("#").append(id).append("\n").append(oldFile);
            //escreve a string no arquivo
            FileOutputStream fos = new FileOutputStream(file, false);
            fos.write(sb.toString().getBytes());
            //fecha as streams
            fos.close();
            reader.close();
            fis.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
