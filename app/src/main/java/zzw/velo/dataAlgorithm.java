package zzw.velo;

/**
 * Created by zzw on 2015/12/20.
 */
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 此类,根据串口协议处理数据请求与发送
 * <pre>
 * [名称]：dataAlgorithm
 * [功能]：数据处理工具类
 * [描述]：无
 * </pre>
 *
 * @author Archer
 * @创建时间 2014-10-26
 */
public class dataAlgorithm {
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString((int) (0xFF & bArray[i]));
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     *[名称]:requestData
     *[功能]:发送命令给指定蓝牙设备
     *[描述]:收到该命令向Android端发送数数据
     *
     * @author archer
     * @创建时间 2014-10-26
     */
    public static byte[] requestData() {
        byte[] data = new byte[8];
        data[0] = (byte) 0xff;
        data[1] = 0x00;
        data[2] = 0x00;
        data[3] = 0x00;
        data[4] = 0x5a;
        data[5] = 0x01;
        data[6] = 0x00;
        data[7] = (byte) (data[0] ^ data[1] ^ data[2] ^ data[3] ^ data[4] ^ data[5] ^ data[6]);//异或
        return data;//64位2进制数
    }

    /**
     *[名称]:requestData1
     *[功能]:清理全部数据,实现关闭
     * @author Archer
     * @创建时间 2014-10-26
     */
    public static byte[] requestData1() {
        byte[] data = new byte[8];
        data[0] = (byte) 0xff;
        data[1] = 0x00;
        data[2] = 0x00;
        data[3] = 0x00;
        data[4] = 0x50;
        data[5] = 0x0000;
        data[6] = 0x0000;
        data[7] = (byte) (data[0] ^ data[1] ^ data[2] ^ data[3] ^ data[4] ^ data[5] ^ data[6]);
        return data;   //64位二进制数
    }

    /**
     *[名称]:errorData
     *[功能]:异常处理
     *
     *[描述]:收到该命令会重复发送数据，不会删除历史?
     *
     * @author xw
     * @创建时间 2013-04-22
     */
    public static byte[] errorData() {
        byte[] data = new byte[8];
        data[0] = (byte) 0xff;
        data[1] = 0x00;
        data[2] = 0x00;
        data[3] = 0x00;
        data[4] = (byte) 0xaa;
        data[5] = 0x0000;
        data[6] = 0x0000;
        data[7] = (byte) (data[0] ^ data[1] ^ data[2] ^ data[3] ^ data[4] ^ data[5] ^ data[6]);
        return data;
    }

    //检查蓝牙数目
    public static int checkData1(String data, byte[] dataByte, int i) {

        if (data == null || data.equals("") || dataByte == null) {
            return -1;
        }

        if(!data.substring(0,2).equals("FF"))
            return -1;
        String tempWeight = data.substring(2, 4);
        String tempTmp=data.substring(4,6);
        int weight = Integer.parseInt(tempWeight, 16);
        int tmp=Integer.parseInt(tempTmp,16);
        if(i==1)
            return weight ;
        else
            return tmp;
    }

    public static byte[] readBytes(InputStream in, int start, int len) {
        if (start >= len) {
            return null;
        }
        ByteArrayOutputStream bo = new ByteArrayOutputStream(); //可以捕获内存缓冲区的数据，转换成字节数组。
        byte[] data = new byte[len];  //新建len长度的字节数组
        byte[] temp = new byte[1];   //新建len的临时字节数组
        int readLen = 0;
        int destPos = start;
        try {
            if ((readLen = in.read(temp)) > -1) {  //从输入流中读取一定数量的字节，存储到temp中，返回字节长度值。如果因为已经到达流末尾而没有可用的字节，则返回值 -1。
                System.arraycopy(temp, 0, data, destPos, readLen);//数组的复制，原数组，原数组复制起始位置，目的数组，目的数组复制起始位置，复制长度
                destPos += readLen;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return bo.toByteArray();
        }

        if (destPos < len) {
            destPos = len;
        }
        bo.write(data, 0, destPos);

        try {
            bo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bo.toByteArray();//字节流转化为byte数组
    }


    public static boolean isRightPwd(String name) {
        boolean isExist = false;

        Pattern p = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9]{5,19}$");
        Matcher m = p.matcher(name);
        boolean b = m.matches();
        if (b) {

            isExist = true;
        } else {
        }
        return isExist;
    }

    public static boolean isNameAdressFormat(String email) {
        boolean isExist = false;

        Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
        Matcher m = p.matcher(email);
        boolean b = m.matches();
        if (b) {
            System.out.println("有效邮件地址");
            isExist = true;
        } else {
            System.out.println("无效邮件地址");
        }
        return isExist;
    }

    public static boolean checkFloat(String num, String type) {
        String eL = "";
        if (type.equals("0+"))
            eL = "^//d+(//.//d+)?$";// 非负浮点数
        else if (type.equals("+"))
            eL = "^((//d+//.//d*[1-9]//d*)|(//d*[1-9]//d*//.//d+)|(//d*[1-9]//d*))$";// 正浮点数
        else if (type.equals("-0"))
            eL = "^((-//d+(//.//d+)?)|(0+(//.0+)?))$";// 非正浮点数
        else if (type.equals("-"))
            eL = "^(-((//d+//.//d*[1-9]//d*)|(//d*[1-9]//d*//.//d+)|(//d*[1-9]//d*)))$";// 负浮点数
        else
            eL = "^(-?//d+)(//.//d+)?$";// 浮点数
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(num);
        boolean b = m.matches();
        return b;
    }

}
