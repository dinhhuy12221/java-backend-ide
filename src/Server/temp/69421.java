import java.util.Scanner;

/**
 *
 * @author vohoa
 */
public class Chuong1BaiTap3 {
    public static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        
        return true;
    }
    public  static int tinhTong(int n) {
        int tong = 0;
        
        for (int i = 1; i < n; i++) {
            if (isPrime(i))
                tong += i;
        }
        
        return tong;
    }
    public static void main(String[] args) {
        int n;
        Scanner sc = new Scanner(System.in);
        
        do {
            System.out.println("Nhap n>=3: ");
            n = sc.nextInt();
            
            if (n <= 2) {
                System.out.println("N>=3 vui long nhap lai");
            }
        } while (n <= 2);
        
        System.out.println("tong cac so nguyen to: " + tinhTong(n));
    }
}