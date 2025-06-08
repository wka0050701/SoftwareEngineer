package utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    /*
        @param input 原始字符串
        @return MD5 哈希值（32位小写)
    */
    public static String encrypt(String input){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for(byte b : digest){
                String hex = String.format("%02x", b);
                sb.append(hex);
            }
            return sb.toString();
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException("MD5算法不可用",e);
        }
    }
}
