import      java.util.*;
public class find_prime {

    public static void main(String[] args) {
        Scanner sc =       new Scanner(     System.in);


                    int low = sc.nextInt()
                    
            , high = sc.nextInt();

                                while (low < high) {
 boolean
             flag = false;

                    for(int i = 2
                    ; i <= low/2; ++i) {
        
                        // condition for nonprime number
                    if
                    (low % i == 0)
                     {
                        flag = true;
                                break;
                }
            }

            if (!flag && low != 0
             && low != 1)
                System.out
                
.print(low + " ");

            ++low;
}
            }
}   