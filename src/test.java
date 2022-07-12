
import java.io.*;
import java.util.Scanner;

public class test {
    static int ASL = 0;
    static int k = 10;
    static int number = k;
    static String path = "1.txt";
    static int no = 0;
    static String phonenum = "";
    static String name = "";
    static String addr = "";
    static double seed = 0.75;
    static Node1[] hashMap1;//线性冲突
    static Node2[] hashMap2;//拉链法
    static class Node2 {
        int no;
        String name;
        String phonenum;
        String addr;
        Node2 next;
        public Node2(int no, String name, String phonenum, String addr) {
            this.no = no;
            this.name = name;
            this.phonenum = phonenum;
            this.addr = addr;
        }
        public String toString() {
            return this.no + "\t" + this.name + "\t" + this.phonenum + "\t" + this.addr;
        }
    }

    static class Node1 {
        int no;
        String name;
        String phonenum;
        String addr;
        public Node1(int no, String name, String phonenum, String addr) {
            this.no = no;
            this.name = name;
            this.phonenum = phonenum;
            this.addr = addr;
        }
        public String toString() {
            return this.no + "\t" + this.name + "\t" + this.phonenum + "\t" + this.addr;
        }
    }

    //生成号码簿
    public static void random(int n) throws IOException {
        File file = new File(path);
        if (file.exists())
            file.delete();
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file.getName(), true);
        for (int k = 0; k < n; k++) {
            number++;
            char xing = (char) (Math.random() * 26 + 97);
            char ming1 = (char) (Math.random() * 26 + 97);
            char ming2 = (char) (Math.random() * 26 + 97);
            char addr1 = (char) (Math.random() * 26 + 65);
            char addr2 = (char) (Math.random() * 26 + 65);
            String phone = "";
            char num1 = (char) (Math.random() * 9 + 49);
            num1 = '1';
            phone = phone + num1;
            for (int i = 0; i < 10; i++) {
                char num = (char) (Math.random() * 10 + 48);
                phone = phone + num;
            }
            String str = number + "\t" + xing + ming1 + ming2 + "\t" + addr1 + addr2 + "\t" + phone + "\n";
            fileWriter.write(str);
        }
        fileWriter.close();
    }
    //电话为关键字 k为解决冲突方式
    public static void readByPhone(int k) throws FileNotFoundException {
        ASL = 0;
        InputStream inputStream = new FileInputStream(path);
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            int phone = 0;
            no = Integer.parseInt(scanner.next());
            name = scanner.next();
            addr = scanner.next();
            phonenum = (scanner.next());
            for (int i = 3; i < phonenum.length(); i++) {
                phone = phone * 10 + (int) (phonenum.charAt(i) - 48);
            }
            int key = phone % (int) (number / seed);
            int len = (int) (number / seed);
            if (k == 1) {
                xianxingTo1(key, len, no, name, phonenum, addr);
            } else if (k == 2) {
                lalianTo2(key, len, no, name, phonenum, addr);
            }
        }
    }
    //姓名为关键字 k为解决冲突方式
    public static void readByName(int k) throws FileNotFoundException {
        ASL = 0;
        InputStream inputStream = new FileInputStream(path);
        Scanner scanner = new Scanner(inputStream);
        int keyseed = 131;
        while (scanner.hasNext()) {
            int namenum = 0;
            no = Integer.parseInt(scanner.next());
            name = scanner.next();
            addr = scanner.next();
            phonenum = (scanner.next());
            for (int i = 0; i < name.length(); i++) {
                namenum = namenum + keyseed * (name.charAt(i));
            }
            int len =    len = hashMap1.length;
            int key  = namenum % len;
            if (k == 1) {
                //线性探测法解决冲突
                xianxingTo1(key, len, no, name, phonenum, addr);
            } else if (k == 2) {
                //拉链法解决冲突
                lalianTo2(key, len, no, name, phonenum, addr);
            }
        }
    }
    //线性探测法解决冲突
    public static void xianxingTo1(int num1, int num2, int no, String name, String phonenum, String addr) {
        int key = num1;
        int temp = 1;
        if (hashMap1[key] == null) {
            Node1 node = new Node1(no, name, phonenum, addr);
            hashMap1[key] = node;
            ASL += temp;
        } else {
            while (hashMap1[key] != null) {
                key++;
                temp++;
                if (key >= num2) {
                    key = 0;
                }
            }
            ASL += temp;
            hashMap1[key] = new Node1(no, name, phonenum, addr);
        }
    }
    //拉链法解决冲突
    public static void lalianTo2(int keyword, int len, int no, String name, String phonenum, String addr) {
        int key = keyword;
        int temp = 1;
        if (hashMap2[key] == null) {
            Node2 node = new Node2(no, name, phonenum, addr);
            hashMap2[key] = node;
            ASL += temp;
            //       System.out.println("冲突次数："+temp+"\tname:"+name);
        } else {
            Node2 node = hashMap2[key];
            while (node.next != null) {
                temp++;
                node = node.next;
            }
            temp++;
            //       System.out.println("冲突次数："+temp+"\tname:"+name);
            ASL += temp;
            node.next = new Node2(no, name, phonenum, addr);
        }
    }

    public static void printhash1() {
        for (int i = 0; i < hashMap1.length; i++) {
            if (hashMap1[i] != null) {
                System.out.println("i:" + i + "\t" + hashMap1[i].toString());
            }
        }
        System.out.println();
    }

    public static void printhash2() {
        for (int i = 0; i < hashMap2.length; i++) {
            Node2 node = hashMap2[i];
            if (node == null) {
                continue;
            }
            System.out.print("i:" + i + "\t" + node.toString());
            while (node.next != null) {
                node = node.next;
                System.out.print("\t" + node.toString());
            }
            System.out.println();
        }
        System.out.println();
    }

    //通过姓名查找  线性探测解决冲突
    public static String findbyname(String name, Node1[] hashMap1) {
        int namenum = 0;
        int seed = 1313;
        for (int i = 0; i < name.length(); i++) {
            namenum = namenum + seed + (name.charAt(i));
        }
        namenum = namenum % hashMap1.length;
        Node1 node = hashMap1[namenum];
        int temp = 0;
        while (true) {
            if (node == null) {
                namenum++;
                if (namenum >= hashMap1.length) {
                    namenum = 0;
                    temp++;
                }
                node = hashMap1[namenum];
            } else if (!node.name.equals(name)) {
                namenum++;
                if (namenum >= hashMap1.length) {
                    namenum = 0;
                    temp++;
                }
                node = hashMap1[namenum];
                if (temp > 10) {
                    System.out.println("no name");
                    break;
                }

            } else if (node.name.equals(name)) {
                System.out.println(node.toString());
                break;
            }
        }
        return node.toString();
    }
    //通过姓名查找 BKDR算法解决冲突
    public static String findbyname(String name, Node2[] hashMap2) {
        int namenum = 0;
        int keyseed = 131;
        for (int i = 0; i < name.length(); i++) {
            namenum = namenum + keyseed + (name.charAt(i));
        }
        namenum = namenum % (int) (number / seed);
        Node2 node = hashMap2[namenum];
        while (!node.name.equals(name)) {
            node = node.next;
        }
        System.out.println(node.toString());
        return node.toString();
    }
    //通过电话查找  线性探测解决冲突
    public static String findbyphone(String phone, Node1[] hashMap1) {
        int phonenum = 0;
        for (int i = 3; i < phone.length(); i++) {
            phonenum = phonenum * 10 + (int) (phone.charAt(i) - 48);
        }
        phonenum = phonenum % number;
        Node1 node = hashMap1[phonenum];
        int temp = 0;
        while (true) {
            if (node == null) {
                phonenum++;
                node = hashMap1[phonenum];
            } else if (!node.phonenum.equals(phone)) {
                phonenum++;
                if (phonenum >= hashMap1.length) {
                    phonenum = 0;
                    temp++;
                }
                node = hashMap1[phonenum];
                if (temp > 10) {
                    System.out.println("no name");
                    break;
                }
            } else if (node.phonenum.equals(phone)) {
                System.out.println(node.toString());
                break;
            }
        }
        return node.toString();

    }
    //通过电话查找  BKDR解决冲突
    public static String findbyphone(String phone, Node2[] hashMap2) {
        int phonenum = 0;
        for (int i = 3; i < phone.length(); i++) {
            phonenum = phonenum * 10 + (int) (phone.charAt(i) - 48);
        }
        phonenum = phonenum % (int) (number / seed);
        Node2 node = hashMap2[phonenum];
        while (!node.phonenum.equals(phone)) {
            node = node.next;
        }
        System.out.println(node.toString());
        return node.toString();
    }

    //生成以姓名为关键字的散列表
    public static void readByName() throws FileNotFoundException {
        ASL = 0;
        number = k;
        hashMap1 = new Node1[(int) (number / seed)];
        hashMap2 = new Node2[(int) (number / seed)];
        readByName(1);
        readByName(2);

    }
    //生成以电话为关键字的散列表
    public static void readByPhone() throws FileNotFoundException {
        ASL = 0;
        number = k;
        hashMap1 = new Node1[(int) (number / seed)];
        hashMap2 = new Node2[(int) (number / seed)];
        readByPhone(1);
        readByPhone(2);
    }


    public static void main(String[] args) throws IOException {
        k = 500;
        random(k);
        number = k;
        seed = 0.9;
        while (true) {
            System.out.println("欢迎进入电话簿查询系统");
            System.out.println("请输入查询方式：1：通过姓名查询 2：通过电话号码查询：");
            Scanner input = new Scanner(System.in);
            int kind = input.nextInt();
            if (kind == 1) {
                readByName();
                System.out.println("请输入姓名：");
                String name = input.next();
                findbyname(name, hashMap1);
                System.out.println("ASL:" + "\t" + ASL * 1.0 / (k));
            } else if (kind == 2) {
                readByPhone();
                System.out.println("请输入电话：");
                String phone = input.next();
                findbyphone(phone, hashMap1);
                System.out.println("ASL:" + ASL + "\t" + ASL * 1.0 / (k));
            } else break;
        }
    }
}
