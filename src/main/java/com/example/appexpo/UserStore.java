package com.example.appexpo;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class UserStore {
    private static final String CSV = "users.csv";
    private static final String SEP = ",";

    public static Map<String,User> loadUsers() {
        Map<String,User> map = new HashMap<>();
        Path p = Paths.get(CSV);
        if (!Files.exists(p)) return map;

        try (BufferedReader br = Files.newBufferedReader(p)) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] col = line.split(SEP);
                if (col.length < 6) continue;
                String email  = col[0];
                String pass   = col[1];
                String name   = col[2];
                int    age    = Integer.parseInt(col[3]);
                double ht     = Double.parseDouble(col[4]);
                double wt     = Double.parseDouble(col[5]);
                map.put(email, new User(email, pass, name, age, ht, wt));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static void addUser(User u) {
        Path p = Paths.get(CSV);
        boolean exists = Files.exists(p);
        try (BufferedWriter bw = Files.newBufferedWriter(
                p,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            if (!exists) {
                bw.write("email,password,name,age,height,weight");
                bw.newLine();
            }
            bw.write(String.join(SEP,
                    u.getEmail(),
                    u.getPassword(),
                    u.getName(),
                    String.valueOf(u.getAge()),
                    String.valueOf(u.getHeight()),
                    String.valueOf(u.getWeight())
            ));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateUser(User updated) {
        Map<String,User> all = loadUsers();
        all.put(updated.getEmail(), updated);

        Path p = Paths.get(CSV);
        try (BufferedWriter bw = Files.newBufferedWriter(p)) {
            bw.write("email,password,name,age,height,weight");
            bw.newLine();
            for (User u : all.values()) {
                bw.write(String.join(SEP,
                        u.getEmail(),
                        u.getPassword(),
                        u.getName(),
                        String.valueOf(u.getAge()),
                        String.valueOf(u.getHeight()),
                        String.valueOf(u.getWeight())
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUser(String email) {
        Map<String,User> all = loadUsers();
        all.remove(email);
        Path p = Paths.get(CSV);
        try (BufferedWriter bw = Files.newBufferedWriter(p)) {
            bw.write("email,password,name,age,height,weight");
            bw.newLine();
            for (User u : all.values()) {
                bw.write(String.join(SEP,
                        u.getEmail(),
                        u.getPassword(),
                        u.getName(),
                        String.valueOf(u.getAge()),
                        String.valueOf(u.getHeight()),
                        String.valueOf(u.getWeight())
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
