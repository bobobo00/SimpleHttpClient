package HTTP;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * @ClassName SimpleHttpClient
 * @Description TODO
 * @Auther danni
 * @Date 2020/1/1 14:37]
 * @Version 1.0
 **/

public class SimpleHttpClient {
    public static void main(String[] args) throws IOException {
        String request="GET / HTTP/1.0\r\nHost:www.baidu.com\r\n\r\n";
        Socket socket=new Socket("www.baidu.com",80);
        socket.getOutputStream().write(request.getBytes("UTF-8"));
        //版本 状态码 状态描述
        //响应头打印
        //响应正文保存下来；
        byte[] bytes=new byte[4096];
        int firstRead=socket.getInputStream().read(bytes);
        int index=-1;
        for (int i = 0; i <firstRead-3 ; i++) {
            if((bytes[i]=='\r')&&(bytes[i+1]=='\n')&&(bytes[i+2]=='\r')&&(bytes[i+3]=='\n')){
                    index=i;
                    break;
            }
        }
        ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(bytes,0,index+4);
        Scanner scanner=new Scanner(byteArrayInputStream,"utf-8");
        String statusLine=scanner.nextLine();
        System.out.println("状态行:"+statusLine);
        String[] group=statusLine.split(" ");
        System.out.println("响应版本："+group[0].trim());
        System.out.println("响应状态码："+group[1].trim());
        System.out.println("响应状态描述："+group[2].trim());
        String line;
        int textLength=-1;
        while(!(line=scanner.nextLine()).isEmpty()){
            String[] kv=line.split(":");
            String key=kv[0].trim();
            String value=kv[1].trim();
            System.out.println("响应头："+key+"="+value);
            if(key.equalsIgnoreCase("Content-Length")){
                textLength=Integer.valueOf(value);
            }
        }
        System.out.println(textLength);
        System.out.println(index);

        int alreadyRead=firstRead-index-4;
        int read=textLength-alreadyRead;
        byte[] body=new byte[textLength];
        System.arraycopy(bytes,index+4,body,0,alreadyRead);
        int actualRead=socket.getInputStream().read(body,alreadyRead,read);
        System.out.println(alreadyRead);
        System.out.println(read);
        System.out.println(actualRead);
        FileOutputStream fileOutputStream=new FileOutputStream("百度.html");
        fileOutputStream.write(body);
        fileOutputStream.flush();

    }
}
